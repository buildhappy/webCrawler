# -*- coding=utf-8 -*-
import os
import sys
import requests
requests.packages.urllib3.disable_warnings() # disable urllib3's warnings https://urllib3.readthedocs.org/en/latest/security.html#insecurerequestwarning
from requests_toolbelt import MultipartEncoder
import urllib
import json
import random
import logging

headers = {
    "Accept":"text/html,application/xhtml+xml,application/xml; " \
        "q=0.9,image/webp,*/*;q=0.8",
    "Accept-Encoding":"text/html",
    "Accept-Language":"en-US,en;q=0.8,zh-CN;q=0.6,zh;q=0.4,zh-TW;q=0.2",
    "Content-Type":"application/x-www-form-urlencoded",
    "Referer":"http://www.baidu.com/",
    "User-Agent":"Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.36 "\
        "(KHTML, like Gecko) Chrome/32.0.1700.77 Safari/537.36"
}

ss = requests.session()
ss.headers.update(headers)

logging.basicConfig(
    level=logging.INFO
    #format="[%(asctime)s]%(name)s:%(levelname)s:%(message)s"
)

class panbaiducom(object):
    #将从页面中获取的信息(info)转换成请求参数保持在params中
    def get_params(self):
        logging.info("in panbaiducom get_params()")
        info = self.infos
        print("info in get_params():")
        for k in info:
            print "info[%s] = " %k , info[k]
        print("\n")

        uk = info['uk']
        shareid = info['shareid']
        timestamp = info['timestamp']
        sign = info['sign']

        self.params = {
            #"bdstoken": bdstoken,
            "uk": uk,
            "shareid": shareid,
            "timestamp": timestamp,
            "sign": sign,
            "channel": "chunlei",
            "clienttype": 0,
            "web": 1,
            "channel": "chunlei",
            "clienttype": 0,
            "web": 1
        }

        fileinfo = info['fileinfo']
        j = json.loads(fileinfo)

        self.infos.update({
            'name': j[0]['server_filename'].encode('utf8'),
            'file': os.path.join(
                os.getcwd(), j[0]['server_filename'].encode('utf8')
            ),
            'dir_': os.getcwd(),
            'fs_id': j[0]['fs_id']
            })

    #获取下载链接信息
    def get_infos(self):
        logging.info("in panbaiducom get_infos()")
        url = 'http://pan.baidu.com/share/download'
        data = 'fid_list=["%s"]' % self.infos['fs_id']
        while True:
            # ss = requests.session()
            r = ss.post(url, data=data, params=self.params)
            print("params in get_infos():")
            for k in self.params:
                print "params[%s]=" % k, self.params[k]
            print "\n"

            j = r.json()
            print("response:%s" % j)
            if not j['errno']:
                self.infos['dlink'] = j['dlink'].encode('utf8')
                print("dlink: %s" % self.infos['dlink'])
                #panbaiducom_HOME._download_do(self.infos)
                return self.infos['dlink']
                #break
            else:
                #有时候需要验证码
                break

    def do(self, info):
        logging.info("in panbaiducom do()")
        self.infos = info
        ss.headers['Referer'] = 'http://pan.baidu.com'
        self.get_params()
        return self.get_infos()

def start(info):
    xw = panbaiducom()
    return xw.do(info)

