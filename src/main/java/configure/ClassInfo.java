package configure;

import org.jdom2.Attribute;
import org.jdom2.Element;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by csun on 10/6/14.
 */
public class ClassInfo {
    private String id;

    public String getClassName() {
        return className;
    }

    private String className;

    public List<Element> getProperties() {
        return properties;
    }

    private List<Element> properties=new ArrayList<Element>();

    public List<Element> getConstructorParams() {
        return constructorParams;
    }

    private List<Element> constructorParams=new ArrayList<Element>();
    public void init(String id, String cName){
        this.id = id;
        this.className = cName;
    }

    public ClassInfo(Element element){
        init( element.getAttributeValue("id"), element.getAttributeValue("class"));
//        List attrs= bean.getAttributes();
//        for (int j=0; j<attrs.size();j++) {
//            Attribute attr = (Attribute)attrs.get(j);
//            String attrName = attr.getName();
//            String attrValue = attr.getValue();
//        }
        List properties= element.getChildren("property");
        if (properties.size() > 0 ) {
            initBySetter(element);
        }else{
            initByConstructor(element);
        }
    }

    private void initBySetter(Element element){

        List props= element.getChildren("property");
        for (int i=0;i< props.size();i++){
            this.properties.add((Element)props.get(i));
        }
    }

    private void initByConstructor(Element element){
        List constructorParams= element.getChildren("constructor-arg");
        for (int i=0;i< constructorParams.size();i++){
            this.constructorParams.add((Element) constructorParams.get(i));
        }
    }

    public boolean isRejectBySetter(){
        return this.properties.size() > 0;
    }

    private void log(Object obj){
        System.out.println(obj);
    }

    @Override
    public String toString() {
        return "configure.ClassInfo{" +
                "id='" + id + '\'' +
                ", className='" + className + '\'' +
                ",size_properties='"+properties.size()+'\''+
                ",size_consPrams='"+constructorParams.size()+'\''+
                '}';
    }
}
