package configure; /**
 * Created by csun on 10/6/14.
 */

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class XmlConfigure {
    public Map<String, ClassInfo> getClassInfoes() {
        return classInfoes;
    }

    private Map<String, ClassInfo> classInfoes = new HashMap<String, ClassInfo>();

    public XmlConfigure(String filename) throws Exception {
        SAXBuilder builder = new SAXBuilder();

        Document doc = builder.build(new File(filename));

        Element element = doc.getRootElement();


        List beans = element.getChildren("bean");


        for (int i = 0; i < beans.size(); i++) {
            Element bean = (Element) beans.get(i);
            ClassInfo classInfo = new ClassInfo(bean);
            classInfoes.put(bean.getAttributeValue("id"), classInfo);

        }


    }

    public static void log(Object obj) {
        System.out.println(obj);
    }

    public void show() {
        Iterator it = classInfoes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            ClassInfo info = (ClassInfo) entry.getValue();
            log(info.toString());

        }
    }


}
