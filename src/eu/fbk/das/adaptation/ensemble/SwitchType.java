//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.16 at 05:58:11 PM CET 
//


package eu.fbk.das.adaptation.ensemble;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * Choose between a list of alternatives or perform a
 * 				default activity
 * 			
 * 
 * <p>Java class for switchType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="switchType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://das.fbk.eu/Process}activityType">
 *       &lt;sequence>
 *         &lt;element name="if" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;choice maxOccurs="2">
 *                     &lt;element name="contextCondition" type="{http://das.fbk.eu/Annotation}PreconditionType"/>
 *                     &lt;element name="varCondition">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                               &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;/sequence>
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                   &lt;/choice>
 *                   &lt;element name="branch">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;group ref="{http://das.fbk.eu/Process}activity" maxOccurs="unbounded"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="default" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="branch">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;group ref="{http://das.fbk.eu/Process}activity" maxOccurs="unbounded"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "switchType", propOrder = {
    "_if",
    "_default"
})
public class SwitchType
    extends ActivityType
{

    @XmlElement(name = "if", required = true)
    protected List<SwitchType.If> _if;
    @XmlElement(name = "default")
    protected SwitchType.Default _default;

    /**
     * Gets the value of the if property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the if property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIf().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SwitchType.If }
     * 
     * 
     */
    public List<SwitchType.If> getIf() {
        if (_if == null) {
            _if = new ArrayList<SwitchType.If>();
        }
        return this._if;
    }

    /**
     * Gets the value of the default property.
     * 
     * @return
     *     possible object is
     *     {@link SwitchType.Default }
     *     
     */
    public SwitchType.Default getDefault() {
        return _default;
    }

    /**
     * Sets the value of the default property.
     * 
     * @param value
     *     allowed object is
     *     {@link SwitchType.Default }
     *     
     */
    public void setDefault(SwitchType.Default value) {
        this._default = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="branch">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;group ref="{http://das.fbk.eu/Process}activity" maxOccurs="unbounded"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "branch"
    })
    public static class Default {

        @XmlElement(required = true)
        protected SwitchType.Default.Branch branch;

        /**
         * Gets the value of the branch property.
         * 
         * @return
         *     possible object is
         *     {@link SwitchType.Default.Branch }
         *     
         */
        public SwitchType.Default.Branch getBranch() {
            return branch;
        }

        /**
         * Sets the value of the branch property.
         * 
         * @param value
         *     allowed object is
         *     {@link SwitchType.Default.Branch }
         *     
         */
        public void setBranch(SwitchType.Default.Branch value) {
            this.branch = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;group ref="{http://das.fbk.eu/Process}activity" maxOccurs="unbounded"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "activity"
        })
        public static class Branch {

            @XmlElements({
                @XmlElement(name = "abstract", type = AbstractType.class),
                @XmlElement(name = "concrete", type = ConcreteType.class),
                @XmlElement(name = "switch", type = SwitchType.class),
                @XmlElement(name = "pick", type = PickType.class),
                @XmlElement(name = "receive", type = ReceiveType.class),
                @XmlElement(name = "invoke", type = InvokeType.class),
                @XmlElement(name = "while", type = WhileType.class)
            })
            protected List<ActivityType> activity;

            /**
             * Gets the value of the activity property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the activity property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getActivity().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link AbstractType }
             * {@link ConcreteType }
             * {@link SwitchType }
             * {@link PickType }
             * {@link ReceiveType }
             * {@link InvokeType }
             * {@link WhileType }
             * 
             * 
             */
            public List<ActivityType> getActivity() {
                if (activity == null) {
                    activity = new ArrayList<ActivityType>();
                }
                return this.activity;
            }

        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;choice maxOccurs="2">
     *           &lt;element name="contextCondition" type="{http://das.fbk.eu/Annotation}PreconditionType"/>
     *           &lt;element name="varCondition">
     *             &lt;complexType>
     *               &lt;complexContent>
     *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                   &lt;sequence>
     *                     &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                     &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;/sequence>
     *                 &lt;/restriction>
     *               &lt;/complexContent>
     *             &lt;/complexType>
     *           &lt;/element>
     *         &lt;/choice>
     *         &lt;element name="branch">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;group ref="{http://das.fbk.eu/Process}activity" maxOccurs="unbounded"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "contextConditionOrVarCondition",
        "branch"
    })
    public static class If {

        @XmlElements({
            @XmlElement(name = "contextCondition", type = PreconditionType.class),
            @XmlElement(name = "varCondition", type = SwitchType.If.VarCondition.class)
        })
        protected List<Object> contextConditionOrVarCondition;
        @XmlElement(required = true)
        protected SwitchType.If.Branch branch;

        /**
         * Gets the value of the contextConditionOrVarCondition property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the contextConditionOrVarCondition property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getContextConditionOrVarCondition().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link PreconditionType }
         * {@link SwitchType.If.VarCondition }
         * 
         * 
         */
        public List<Object> getContextConditionOrVarCondition() {
            if (contextConditionOrVarCondition == null) {
                contextConditionOrVarCondition = new ArrayList<Object>();
            }
            return this.contextConditionOrVarCondition;
        }

        /**
         * Gets the value of the branch property.
         * 
         * @return
         *     possible object is
         *     {@link SwitchType.If.Branch }
         *     
         */
        public SwitchType.If.Branch getBranch() {
            return branch;
        }

        /**
         * Sets the value of the branch property.
         * 
         * @param value
         *     allowed object is
         *     {@link SwitchType.If.Branch }
         *     
         */
        public void setBranch(SwitchType.If.Branch value) {
            this.branch = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;group ref="{http://das.fbk.eu/Process}activity" maxOccurs="unbounded"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "activity"
        })
        public static class Branch {

            @XmlElements({
                @XmlElement(name = "abstract", type = AbstractType.class),
                @XmlElement(name = "concrete", type = ConcreteType.class),
                @XmlElement(name = "switch", type = SwitchType.class),
                @XmlElement(name = "pick", type = PickType.class),
                @XmlElement(name = "receive", type = ReceiveType.class),
                @XmlElement(name = "invoke", type = InvokeType.class),
                @XmlElement(name = "while", type = WhileType.class)
            })
            protected List<ActivityType> activity;

            /**
             * Gets the value of the activity property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the activity property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getActivity().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link AbstractType }
             * {@link ConcreteType }
             * {@link SwitchType }
             * {@link PickType }
             * {@link ReceiveType }
             * {@link InvokeType }
             * {@link WhileType }
             * 
             * 
             */
            public List<ActivityType> getActivity() {
                if (activity == null) {
                    activity = new ArrayList<ActivityType>();
                }
                return this.activity;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "name",
            "value"
        })
        public static class VarCondition {

            @XmlElement(required = true)
            protected String name;
            @XmlElement(required = true)
            protected String value;

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
             * Gets the value of the value property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getValue() {
                return value;
            }

            /**
             * Sets the value of the value property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setValue(String value) {
                this.value = value;
            }

        }

    }

}
