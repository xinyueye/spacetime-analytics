package cug.tools;

import java.util.Properties;


public final class Config {

        private static Properties prop = new Properties();

        public static String getStringProperty(String key, String defaultValue) {
                if (prop == null || prop.getProperty(key) == null) {
                        return defaultValue;
                }
                return prop.getProperty(key);
        }

        public static int getIntProperty(String key, int defaultValue) {
                if (prop == null || prop.getProperty(key) == null) {
                        return defaultValue;
                }
                return Integer.parseInt(prop.getProperty(key));
        }

        public static long getLongProperty(String key, long defaultValue) {
                if (prop == null || prop.getProperty(key) == null) {
                        return defaultValue;
                }
                return Long.parseLong(prop.getProperty(key));
        }
        
        public static boolean getBooleanProperty(String key, boolean defaultValue) {
                if (prop == null || prop.getProperty(key) == null) {
                        return defaultValue;
                }
                return prop.getProperty(key).toLowerCase().trim().equals("true");
        }

        static {
                try {
                        prop.load(Config.class.getClassLoader()
                                        .getResourceAsStream("config.properties"));
                } catch (Exception e) {
                        prop = null;
                        System.err.println("WARNING: Could not find config.properties file in class path. I will use the default values.");
                }
        }
}