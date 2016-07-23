//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.02.14 at 03:14:15 PM CET 
//


package model.queries;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 
 *         @Target({}) @Retention(RUNTIME)
 *         public @interface NamedAttributeNode {
 *           String value();
 *           String subgraph() default "";
 *           String keySubgraph() default "";
 *         }
 *  
 *       
 * 
 * <p>Java class for named-attribute-node complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="named-attribute-node">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="subgraph" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="key-subgraph" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "named-attribute-node")
public class NamedAttributeNode {

    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "subgraph")
    protected String subgraph;
    @XmlAttribute(name = "key-subgraph")
    protected String keySubgraph;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the subgraph property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubgraph() {
        return subgraph;
    }

    /**
     * Sets the value of the subgraph property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubgraph(String value) {
        this.subgraph = value;
    }

    /**
     * Gets the value of the keySubgraph property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeySubgraph() {
        return keySubgraph;
    }

    /**
     * Sets the value of the keySubgraph property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeySubgraph(String value) {
        this.keySubgraph = value;
    }

}
