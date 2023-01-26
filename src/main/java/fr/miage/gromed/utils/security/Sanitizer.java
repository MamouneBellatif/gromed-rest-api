package fr.miage.gromed.utils.security;
import io.micrometer.core.instrument.util.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.*;
import org.apache.commons.*;
import org.springframework.util.StringUtils;
public class Sanitizer {

//    public static String cleanIt(String str) {
//        return Jsoup.clean(
//                StringEscapeUtils.escapeJson((StringUtils.replace(str, "'", "''")))
//                , Whitelist.basic());
//    }
}
