package cn.weihu.base.result;

public class CheckAuthUtils {/*
	private static Logger log = Logger.getLogger("api");
	public static void checkAuth(String companyid,Object[] params) {
		List<String> paramsarr = new ArrayList<String>();
		paramsarr.add(companyid);
		String sign = params[1].toString();
		if (sign == null || sign.isEmpty()) {
			throw new AuthException("签名为空");
		}
		for (int i=0,keylen = params.length;i<keylen;i++) {
			if(params[i]!=null && i != 1)
			paramsarr.add(URLDecoder.decode(params[i].toString()));
		}
		log.info("请求参数：" + StringUtils.join(paramsarr, ','));
		if (companyid == null || companyid.isEmpty()) {
			throw new AuthException("参数中没有企业id");
		}
		Collections.sort(paramsarr, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				if (o1 == null || o2 == null) {
					return -1;
				}
				return o1.compareTo(o2);
			}
		});
//		String token = Staticdata.companytokens.get(companyid);
//		if (token == null || token.isEmpty()) {
//			throw new AuthException("未获取到token");
//		}
//		paramsarr.add(token);
		String queryString = StringUtils.join(paramsarr);
		String md5str = MD5Utils.md5(queryString);
		if (!md5str.equals(sign)) {
			log.info("计算签名："+md5str+",传入签名："+sign+",token:");
			throw new AuthException("签名验证失败");
		}
	}
	public static void checkAuth(String companyid,Map<String,String[]> params) {
		List<String> paramsarr = new ArrayList<String>();
		paramsarr.add(companyid);
		String sign = params.get("sign")!=null?params.get("sign")[0]:null;
		if (sign == null || sign.isEmpty()) {
			throw new AuthException("签名为空");
		}
		for (Map.Entry<String, String[]> entry:params.entrySet()) {
			if(!entry.getKey().equals("sign"))
			paramsarr.add(URLDecoder.decode(entry.getValue()[0]));
		}
		log.info("paramsarr====" + StringUtils.join(paramsarr, ','));
		if (companyid == null || companyid.isEmpty()) {
			throw new AuthException("参数中没有企业id");
		}
		
		Collections.sort(paramsarr, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				if (o1 == null || o2 == null) {
					return -1;
				}
				return o1.compareTo(o2);
			}
		});
//		String token = Staticdata.companytokens.get(companyid);
//		if (token == null || token.isEmpty()) {
//			throw new AuthException("未获取到token");
//		}
//		paramsarr.add(token);
		String queryString = StringUtils.join(paramsarr);
		String md5str = MD5Utils.md5(queryString);
		if (!md5str.equals(sign)) {
			log.info("计算签名："+md5str+",传入签名："+sign+",token:");
			throw new AuthException("签名验证失败");
		}
	}
*/
}
