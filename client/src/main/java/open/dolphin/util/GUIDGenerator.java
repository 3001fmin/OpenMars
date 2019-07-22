package open.dolphin.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class GUIDGenerator {
    
    /** Cached per JVM server IP. */
    private static String hexServerIP = null;
    
    // initialise the secure random instance
    private static final java.security.SecureRandom seeder = new java.security.SecureRandom();
    
    /**
     * A 32 byte GUID generator (Globally Unique ID). These artificial keys
     * SHOULD <strong>NOT </strong> be seen by the user, not even touched by the
     * DBA but with very rare exceptions, just manipulated by the database and
     * the programs.
     *
     * Usage: Add an id field (type java.lang.String) to your EJB, and add
     * setId(XXXUtil.generateGUID(this)); to the ejbCreate method.
     * @param o
     * @return 
     */
    public static String generate(Object o) {
        StringBuilder tmpBuffer = new StringBuilder(16);
        if (hexServerIP == null) {
            java.net.InetAddress localInetAddress;
            try {
                // get the inet address
        
                localInetAddress = java.net.InetAddress.getLocalHost();
            } catch (java.net.UnknownHostException uhe) {
                System.err.println("KingAppoUtil: Could not get the local IP address using InetAddress.getLocalHost()!");
                // todo: find better way to get around this...
                uhe.printStackTrace(System.err);
                // Mavericks対応
                //return null;
                String uuid = UUID.randomUUID().toString();
                uuid = uuid.replaceAll("-", "");
                if(uuid.length() != 32) {
                    SimpleDateFormat effectiveFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                    uuid = effectiveFormat.format(new Date()) + "000000000000000";
                }
                return uuid;
            }
            byte[] serverIP = localInetAddress.getAddress();
            hexServerIP = hexFormat(getInt(serverIP), 8);
        }
        
        String hashcode = hexFormat(System.identityHashCode(o), 8);
        tmpBuffer.append(hexServerIP);
        tmpBuffer.append(hashcode);
        
        long timeNow = System.currentTimeMillis();
        int timeLow = (int) timeNow & 0xFFFFFFFF;
        int node = seeder.nextInt();

        String guid = hexFormat(timeLow, 8) +
                tmpBuffer.toString() +
                hexFormat(node, 8);
        return guid;
    }
    
    private static int getInt(byte[] bytes) {
        int i = 0;
        int j = 24;
        for (int k = 0; j >= 0; k++) {
            int l = bytes[k] & 0xff;
            i += l << j;
            j -= 8;
        }
        return i;
    }
    
    private static String hexFormat(int i, int j) {
        String s = Integer.toHexString(i);
        return padHex(s, j) + s;
    }
    
    private static String padHex(String s, int i) {
        StringBuilder tmpBuffer = new StringBuilder();
        if (s.length() < i) {
            for (int j = 0; j < i - s.length(); j++) {
                tmpBuffer.append('0');
            }
        }
        return tmpBuffer.toString();
    }
    
}
