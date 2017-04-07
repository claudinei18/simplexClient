/**
 * Created by claudinei on 21/02/17.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 */
public class Crawler {
    private static Map<InetAddress, String> hmapDns;


    /**
     * @param dominio
     * @return
     */
    public static InetAddress getIp(String dominio) {
        try {
            return InetAddress.getByName(new URL(dominio).getHost());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getHtmlContent(String dominio) {
        String content = null;
        URLConnection connection = null;
        try {
            URL url = new URL(dominio);
            connection = url.openConnection();

            String nameFile = url.getHost();

            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            content = scanner.next();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return content;
    }


    /**
     * @param args
     */
    public static void main(String[] args) {
        hmapDns = new HashMap<InetAddress, String>();


//        try{
//            BufferedWriter bw = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/cachedns/ips.txt"));
//
//            bw.write(content);
//            bw.close();


//            System.out.println(getIp("www.pucminas.br").getHostAddress());
//            getHtmlContent("http://www.pucminas.br");
//
//            getIp("www.google.com.br");
//            getHtmlContent("http://www.google.com.br");
//
//            getIp("www.globo.com.br");
//            getIp("www.facebook.com.br");
//
//            getIp("www.pudim.com.br");
//            getHtmlContent("http://www.pudim.com.br");
//
//            getIp("www.icei.pucminas.br");
//            getIp("sgl.icei.pucminas.br");
//            getIp("claudineigomes.ddns.net");
//            getIp("dehuachen.ddns.net");
//            getIp("pucminas.br");
//
//        }catch(Exception e){
//            e.printStackTrace();
//        }

        try {
            String read = null;
            BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/domainslist/domains.txt"));
            BufferedWriter bw = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/cachedns/ips.txt"));
            BufferedWriter bwHtmlContent = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/files/file.txt"));

            while ((read = br.readLine()) != null) {

                bwHtmlContent.write(getHtmlContent(read));
                hmapDns.put(getIp(read), read);

                bw.write(getIp(read).getHostAddress() + " " + read + "\n");
            }
            br.close();
            bw.close();
            bwHtmlContent.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}