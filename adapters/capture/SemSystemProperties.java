package android.os;
public final class SemSystemProperties{
 public static String getSalesCode(){return get("ro.csc.sales_code", get("ro.boot.sales_code", ""));}

 public static String getCountryIso(){return get("ro.csc.countryiso_code", "");}

 public static String get(String key){return get(key,"");}public static String get(String key,String def){try{return (String)Class.forName("android.os.SystemProperties").getMethod("get",String.class,String.class).invoke(null,key,def);}catch(Exception ex){return def;}}
 public static int getInt(String key,int def){try{return Integer.parseInt(get(key));}catch(Exception ex){return def;}}public static long getLong(String key,long def){try{return Long.parseLong(get(key));}catch(Exception ex){return def;}}public static boolean getBoolean(String key,boolean def){String value=get(key,"").toLowerCase();if(value.equals("1")||value.equals("true")||value.equals("yes")||value.equals("on"))return true;if(value.equals("0")||value.equals("false")||value.equals("no")||value.equals("off"))return false;return def;}}
