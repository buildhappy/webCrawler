package us.codecraft.webmagic;

import com.appCrawler.utils.HttpSendData;
import com.appCrawler.utils.JsonUtils;
import com.appCrawler.utils.ToFileSendData;
import com.google.common.collect.Lists;

import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.thread.CountableThreadPool;
import us.codecraft.webmagic.utils.UrlUtils;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Entrance of a crawler.<br>
 * A spider contains four modules: Downloader, Scheduler, PageProcessor and
 * Pipeline.<br>
 * Every module is a field of Spider. <br>
 * The modules are defined in interface. <br>
 * You can customize a spider with various implementations of them. <br>
 * Examples: <br>
 * <br>
 * A simple crawler: <br>
 * Spider.create(new SimplePageProcessor("http://my.oschina.net/",
 * "http://my.oschina.net/*blog/*")).run();<br>
 * <br>
 * Store results to files by FilePipeline: <br>
 * Spider.create(new SimplePageProcessor("http://my.oschina.net/",
 * "http://my.oschina.net/*blog/*")) <br>
 * .pipeline(new FilePipeline("/data/temp/webmagic/")).run(); <br>
 * <br>
 * Use FileCacheQueueScheduler to store urls and cursor in files, so that a
 * MySpider can resume the status when shutdown. <br>
 * Spider.create(new SimplePageProcessor("http://my.oschina.net/",
 * "http://my.oschina.net/*blog/*")) <br>
 * .scheduler(new FileCacheQueueScheduler("/data/temp/webmagic/cache/")).run(); <br>
 * 
 * @author code4crafter@gmail.com <br>
 * @see Downloader
 * @see Scheduler
 * @see PageProcessor
 * @see Pipeline
 * @since 0.1.0
 */
public class Spider implements Runnable, Task {

    protected Downloader downloader;

    protected PageProcessor pageProcessor;

    protected List<Request> startRequests;

    protected Site site;

    protected String uuid;

    protected BlockingQueue<Request> toDoQueue;//存放待抓取的url
    protected BlockingQueue<Request> doneQueue;//存放待抓取的url

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected CountableThreadPool threadPool;

    protected ExecutorService executorService;//线程池

    protected int threadNum = 3;

    protected AtomicInteger stat = new AtomicInteger(STAT_INIT);//线程安全的

    protected boolean exitWhenComplete = true;

    protected final static int STAT_INIT = 0;

    protected final static int STAT_RUNNING = 1;

    protected final static int STAT_STOPPED = 2;

    protected boolean spawnUrl = true;

    protected boolean destroyWhenExit = true;

    private ReentrantLock newUrlLock = new ReentrantLock();

    private Condition newUrlCondition = newUrlLock.newCondition();

    private List<SpiderListener> spiderListeners;

    private final AtomicLong pageCount = new AtomicLong(0);

    private Date startTime;

    private int emptySleepTime = 30000;
    
    /**
     * the id of task
     */
    private String taskId;
    
    /**
     * channel id
     */
    private String channelId;
    
    /**
     * container for app information
     */
    private BlockingQueue<Apk> appsInfo;
    
    /**
     * the address of remote user, for sending data
     */
    private String remoteIp = "127.0.0.1";
    private ToFileSendData printDataToFile;
	/**
     * taskId setter
     * @param taskId
     */
    public void setTaskId(String taskId){
    	this.taskId = taskId;
    }
    
    public String getTaskId(){
    	return this.taskId;
    }

	static{
		System.setProperty("http.proxyHost", "proxy.buptnsrc.com");
		System.setProperty("https.proxyHost", "proxy.buptnsrc.com");
		System.setProperty("http.proxyPort", "8001");
		System.setProperty("https.proxyPort", "8001");
		//threadPool = new CountableThreadPool(threadNum);
	}
	
	
    /**
     * create a spider with pageProcessor.
     *
     * @param pageProcessor
     * @return new spider
     * @see PageProcessor
     */
    public static Spider create(PageProcessor pageProcessor) {
    	BlockingQueue<Apk> infos = new LinkedBlockingQueue<Apk>();
        return new Spider(pageProcessor , infos);
    }
    
    /**
     * create a spider with pageProcessor.
     *
     * @param pageProcessor
     * @return new spider
     * @see PageProcessor
     */
    public static Spider create(PageProcessor pageProcessor , BlockingQueue<Apk> infos) {
        return new Spider(pageProcessor , infos);
    }
    
    /**
     * create a spider with pageProcessor,taskId and channelId
     * @param pageProcessor
     * @param taskId
     * @param channelId
     */
    public Spider(PageProcessor pageProcessor , String taskId , String channelId){
    	initContainer();
    	this.taskId = taskId;
    	this.channelId = channelId;
    	this.pageProcessor = pageProcessor;
    	this.site = pageProcessor.getSite();
    	
    }
    /**
     * create a spider with pageProcessor,taskId , channelId and remoteAddress
     * @param pageProcessor
     * @param taskId
     * @param channelId
     * @param remoteAddress
     */
    public Spider(PageProcessor pageProcessor , String taskId , String channelId , String remoteIp){
    	initContainer();
    	this.taskId = taskId;
    	this.channelId = channelId;
    	this.pageProcessor = pageProcessor;
    	this.site = pageProcessor.getSite();
    	this.remoteIp = remoteIp;
    	
    }
    /**
     * create a spider with pageProcessor and Apk container
     * @param pageProcessor
     * @param infos
     */
    public Spider(PageProcessor pageProcessor , BlockingQueue<Apk> infos) {
    	initContainer();
        this.pageProcessor = pageProcessor;
        this.site = pageProcessor.getSite();
        this.startRequests = pageProcessor.getSite().getStartRequests();
        this.appsInfo = infos;
        this.toDoQueue = new LinkedBlockingQueue<Request>();
    }
    
    /**
     * initialize the container
     */
    private void initContainer(){
    	this.appsInfo = new LinkedBlockingQueue<Apk>();
    	this.toDoQueue = new LinkedBlockingQueue<Request>();
    	this.doneQueue = new LinkedBlockingQueue<Request>();
    }

    /**
     * Set startUrls of Spider.<br>
     * Prior to startUrls of Site.
     *
     * @param startUrls
     * @return this
     */
    public Spider startUrls(List<String> startUrls) {
        checkIfRunning();
        this.startRequests = UrlUtils.convertToRequests(startUrls);
        return this;
    }

    /**
     * Set startUrls of Spider.<br>
     * Prior to startUrls of Site.
     *
     * @param startRequests
     * @return this
     */
    public Spider startRequest(List<Request> startRequests) {
        checkIfRunning();
        this.startRequests = startRequests;
        return this;
    }

    /**
     * Set an uuid for spider.<br>
     * Default uuid is domain of site.<br>
     *
     * @param uuid
     * @return this
     */
    public Spider setUUID(String uuid) {
        this.uuid = uuid;
        return this;
    }

    /**
     * set the downloader of spider
     *
     * @param downloader
     * @return this
     * @see #setDownloader(us.codecraft.webmagic.downloader.Downloader)
     * @deprecated
     */
    public Spider downloader(Downloader downloader) {
        return setDownloader(downloader);
    }

    /**
     * set the downloader of spider
     *
     * @param downloader
     * @return this
     * @see Downloader
     */
    public Spider setDownloader(Downloader downloader) {
        checkIfRunning();
        this.downloader = downloader;
        return this;
    }

    protected void initComponent() {
        if (downloader == null) {
            this.downloader = new HttpClientDownloader();//默认使用的Downloader
        }
        downloader.setThread(threadNum);//pageDownloader
        if (threadPool == null || threadPool.isShutdown()) {
            if (executorService != null && !executorService.isShutdown()) {
                threadPool = new CountableThreadPool(threadNum, executorService);
            } else {
                threadPool = new CountableThreadPool(threadNum);
            }
        }
        if (startRequests != null) {
            for (Request request : startRequests) {
                pushTodoQueue(request , this);
            }
            startRequests.clear();
        }
        startTime = new Date();
    }

    //@Override
    public void run() {
        checkRunningStat();
        initComponent();
        logger.info("Spider " + getUUID() + " started!");
        while (!Thread.currentThread().isInterrupted() && stat.get() == STAT_RUNNING) {
//            Request request = scheduler.poll(this);
            Request request = pollToDoQueue(this);
            if (request == null) {
                if (threadPool.getThreadAlive() == 0 && exitWhenComplete) {
                    break;
                }
                // wait until new url added
                waitNewUrl();
            } else {
                final Request requestFinal = request;
                threadPool.execute(new Runnable() {
                    public void run() {
                        try {
                            processRequest(requestFinal);
                            onSuccess(requestFinal);
                        } catch (Exception e) {
                        	logger.error(e.toString());
                            onError(requestFinal);
                            logger.error("process request " + requestFinal + " error", e);
                        } finally {
                            /*if (site.getHttpProxyPool().isEnable()) {
                                site.returnHttpProxyToPool((HttpHost) requestFinal.getExtra(Request.PROXY), (Integer) requestFinal
                                        .getExtra(Request.STATUS_CODE));
                            }*/
                            pageCount.incrementAndGet();
                            signalNewUrl();
                        }
                    }
                });
            }
        }
        stat.set(STAT_STOPPED);
        try {
			sendData();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			logger.error(e.toString());
			e.printStackTrace();
		}
        // release some resources
        if (destroyWhenExit) {
            close();
        }
    }

    protected void onError(Request request) {
        if (CollectionUtils.isNotEmpty(spiderListeners)) {
            for (SpiderListener spiderListener : spiderListeners) {
                spiderListener.onError(request);
            }
        }
    }

    protected void onSuccess(Request request) {
        if (CollectionUtils.isNotEmpty(spiderListeners)) {
            for (SpiderListener spiderListener : spiderListeners) {
                spiderListener.onSuccess(request);
            }
        }
    }

    private void checkRunningStat() {
        while (true) {
            int statNow = stat.get();
            if (statNow == STAT_RUNNING) {
                throw new IllegalStateException("Spider is already running!");
            }
            if (stat.compareAndSet(statNow, STAT_RUNNING)) {
                break;
            }
        }
    }

    /**
     * the crawl task is finished and send the results
     */
    public void close() {
    	logger.info("Spider close()");
        destroyEach(downloader);
        destroyEach(pageProcessor);
        threadPool.shutdown();
    }
    
    /**
     * send data the the remote user
     * @throws UnknownHostException 
     */
    public void sendData() throws UnknownHostException{
    	logger.info("Spider sendData()");
        //send data to remote user
    	InetAddress currentAddress = InetAddress.getLocalHost();
    	String header = "";
    	String toSendData = JsonUtils.getSenderData(currentAddress.toString() , channelId ,header, "4" , appsInfo);
    	HttpSendData httpSendData = new HttpSendData();
    	logger.info("Spider sendData():" + toSendData);
    	httpSendData.sendData(remoteIp , taskId , toSendData);
    	
    	//write data to file
    	synchronized(Spider.class){
    		//printDataToFile = new ToFileSendData(this.channelId);
        	//printDataToFile.sendData("./" , "channelId:" + channelId);
            while(!appsInfo.isEmpty()){
            	Apk apk = appsInfo.remove();
        		//printDataToFile.sendData("./" , apk.getAppName() + ":" + apk.getAppDownloadUrl());
            	logger.info("name:" + apk.getAppName());
            	logger.info("metaUrl:" + apk.getAppMetaUrl());
            	logger.info("commentUrl:" + apk.getAppCommentUrl());
            	logger.info("osPlatform:" + apk.getOsPlatform());
            	logger.info("download url:" + apk.getAppDownloadUrl());
        		System.out.println();
            }
           // printDataToFile.printDone();
    	}
    }

    private void destroyEach(Object object) {
    	
        if (object instanceof Closeable) {
            try {
                ((Closeable) object).close();
            } catch (IOException e) {
            	logger.error(e.toString());
                e.printStackTrace();
            }
        }
    }

    /**
     * Process specific urls without url discovering.
     *
     * @param urls urls to process
     */
    public void test(String... urls) {
        initComponent();
        if (urls.length > 0) {
            for (String url : urls) {
                processRequest(new Request(url));
            }
        }
    }

    protected void processRequest(Request request) {
        Page page = downloader.download(request, this);
        if (page == null) {
            sleep(site.getSleepTime());
            onError(request);
            return;
        }
        // for cycle retry
        if (page.isNeedCycleRetry()) {
            extractAndAddRequests(page, true);
            sleep(site.getSleepTime());
            return;
        }
        Apk apk = pageProcessor.process(page);
        
        if(apk != null){
        	//apk.setChannelId(channelId);
        	if(!appsInfo.contains(apk)){
        		appsInfo.add(apk);//add apk to the appsInfo
        	}
        	
        }else{
        	List<Apk> apks = pageProcessor.processMulti(page);
        	if(apks != null){
        		for(Apk curApk:apks){
        			if(curApk.getAppDownloadUrl() != null){
        				appsInfo.add(curApk);
        			}
        		}
        		//appsInfo.addAll(apks);
        	}
        }
        extractAndAddRequests(page, spawnUrl);
        sleep(site.getSleepTime());
    }

    protected void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //将page中的targetRequest添加到Scheduler中
    protected void extractAndAddRequests(Page page, boolean spawnUrl) {
        if (spawnUrl && CollectionUtils.isNotEmpty(page.getTargetRequests())) {
            for (Request request : page.getTargetRequests()) {
                addRequest(request);
            }
        }
    }

    private void addRequest(Request request) {
        if (site.getDomain() == null && request != null && request.getUrl() != null) {
            site.setDomain(UrlUtils.getDomain(request.getUrl()));
        }
        pushTodoQueue(request , this);
    }

    protected void checkIfRunning() {
        if (stat.get() == STAT_RUNNING) {
            throw new IllegalStateException("Spider is already running!");
        }
    }

    public void runAsync() {
        Thread thread = new Thread(this);
        thread.setDaemon(false);
        thread.start();
    }

    /**
     * Add urls to crawl. <br/>
     *
     * @param urls
     * @return
     */
    public Spider addUrl(String... urls) {
        for (String url : urls) {
            addRequest(new Request(url));
        }
        signalNewUrl();
        return this;
    }


    /**
     * Add urls with information to crawl.<br/>
     *
     * @param requests
     * @return
     */
    public Spider addRequest(Request... requests) {
        for (Request request : requests) {
            addRequest(request);
        }
        signalNewUrl();
        return this;
    }

    private void waitNewUrl() {
        newUrlLock.lock();
        try {
            //double check
            if (threadPool.getThreadAlive() == 0 && exitWhenComplete) {
                return;
            }
            newUrlCondition.await(emptySleepTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.warn("waitNewUrl - interrupted, error {}", e);
        } finally {
            newUrlLock.unlock();
        }
    }

    private void signalNewUrl() {
        try {
            newUrlLock.lock();
            newUrlCondition.signalAll();
        } finally {
            newUrlLock.unlock();
        }
    }

    public void start() {
        runAsync();
    }

    public void stop() {
        if (stat.compareAndSet(STAT_RUNNING, STAT_STOPPED)) {
            logger.info("Spider " + getUUID() + " stop success!");
        } else {
            logger.info("Spider " + getUUID() + " stop fail!");
        }
    }

    /**
     * start with more than one threads
     *
     * @param threadNum
     * @return this
     */
    public Spider thread(int threadNum) {
        checkIfRunning();
        this.threadNum = threadNum;
        if (threadNum <= 0) {
            throw new IllegalArgumentException("threadNum should be more than one!");
        }
        return this;
    }

    /**
     * start with more than one threads
     *
     * @param threadNum
     * @return this
     */
    public Spider thread(ExecutorService executorService, int threadNum) {
        checkIfRunning();
        this.threadNum = threadNum;
        if (threadNum <= 0) {
            throw new IllegalArgumentException("threadNum should be more than one!");
        }
        return this;
    }

    public boolean isExitWhenComplete() {
        return exitWhenComplete;
    }

    /**
     * Exit when complete. <br/>
     * True: exit when all url of the site is downloaded. <br/>
     * False: not exit until call stop() manually.<br/>
     *
     * @param exitWhenComplete
     * @return
     */
    public Spider setExitWhenComplete(boolean exitWhenComplete) {
        this.exitWhenComplete = exitWhenComplete;
        return this;
    }

    public boolean isSpawnUrl() {
        return spawnUrl;
    }

    /**
     * Get page count downloaded by spider.
     *
     * @return total downloaded page count
     * @since 0.4.1
     */
    public long getPageCount() {
        return pageCount.get();
    }

    /**
     * Get running status by spider.
     *
     * @return running status
     * @see Status
     * @since 0.4.1
     */
    public Status getStatus() {
        return Status.fromValue(stat.get());
    }


    public enum Status {
        Init(0), Running(1), Stopped(2);

        private Status(int value) {
            this.value = value;
        }

        private int value;

        int getValue() {
            return value;
        }

        public static Status fromValue(int value) {
            for (Status status : Status.values()) {
                if (status.getValue() == value) {
                    return status;
                }
            }
            //default value
            return Init;
        }
    }

    /**
     * Get thread count which is running
     *
     * @return thread count which is running
     * @since 0.4.1
     */
    public int getThreadAlive() {
        if (threadPool == null) {
            return 0;
        }
        return threadPool.getThreadAlive();
    }

    /**
     * Whether add urls extracted to download.<br>
     * Add urls to download when it is true, and just download seed urls when it is false. <br>
     * DO NOT set it unless you know what it means!
     *
     * @param spawnUrl
     * @return
     * @since 0.4.0
     */
    public Spider setSpawnUrl(boolean spawnUrl) {
        this.spawnUrl = spawnUrl;
        return this;
    }

    //@Override
    public String getUUID() {
        if (uuid != null) {
            return uuid;
        }
        if (site != null) {
            return site.getDomain();
        }
        uuid = UUID.randomUUID().toString();
        return uuid;
    }

    public Spider setExecutorService(ExecutorService executorService) {
        checkIfRunning();
        this.executorService = executorService;
        return this;
    }

    //@Override
    public Site getSite() {
        return site;
    }

    public List<SpiderListener> getSpiderListeners() {
        return spiderListeners;
    }

    public Spider setSpiderListeners(List<SpiderListener> spiderListeners) {
        this.spiderListeners = spiderListeners;
        return this;
    }

    public Date getStartTime() {
        return startTime;
    }

    /**
     * Set wait time when no url is polled.<br></br>
     *
     * @param emptySleepTime In MILLISECONDS.
     */
    public void setEmptySleepTime(int emptySleepTime) {
        this.emptySleepTime = emptySleepTime;
    }
    
    
    /**
     * get request from the job toDoQueue
     */
    public synchronized Request pollToDoQueue(Task task){
    	Request request = toDoQueue.poll();
    	pushDoneQueue(request , task);
    	return request;//Ordered to fetch
    }
    
    /**
     * push request from the job toDoQueue
     * @param request
     */
    public synchronized void pushTodoQueue(Request request , Task task){
    	if(!isDoneRequest(request , this) && !toDoQueue.contains(request)){
    		toDoQueue.add(request);
    	}
    }
    
    /**
     * push requet to the job done queue 
     * @param request
     * @param task
     */
    public synchronized void pushDoneQueue(Request request, Task task){
    	if(request != null){
    		doneQueue.add(request);
    	}
    }
    
    /**
     * get requet to the job done queue 
     * @param task
     * @return
     */
    public synchronized Request pollDoneQueue(Task task){
    	return doneQueue.poll();
    }
    
    /**
     * is the request contains in the job done queue
     * @param request
     * @param task
     * @return
     */
    public synchronized boolean isDoneRequest(Request request , Task task){
    	return doneQueue.contains(request);
    }
}
