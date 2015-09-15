package us.codecraft.webmagic;

/**
 * java bean for user request encapsulation
 * @author buildhappy
 *
 */
public class UserRequest {
	private String channelId;
	
	private String keyword;
	
	private String version;
	
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
}
