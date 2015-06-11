/**
 * UVCONNECTORBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package UVCONNECTOR.services.applic.uniface;

public class UVCONNECTORBindingStub extends org.apache.axis.client.Stub implements UVCONNECTOR.services.applic.uniface.UVCONNECTORPortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[83];
        _initOperationDesc1();
        _initOperationDesc2();
        _initOperationDesc3();
        _initOperationDesc4();
        _initOperationDesc5();
        _initOperationDesc6();
        _initOperationDesc7();
        _initOperationDesc8();
        _initOperationDesc9();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ADDPORTALPORTLET");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTLET", "ABSTRACTPORTLETArray"), ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PPACKAGEID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTLET", "ABSTRACTPORTLETArray"), ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ADDPORTALUSER");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_USER", "ABSTRACTPORTAL_USERArray"), ABSTRACTPORTAL_USER.services.applic.uniface.ABSTRACTPORTAL_USERType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PPACKAGEID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("AUTHENTICATEPORTALUSER");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PUSERID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PPASSWORD"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDCOOKIE_CONTEXT", "STANDARDCOOKIE_CONTEXTType"), STANDARDCOOKIE_CONTEXT.services.applic.uniface.STANDARDCOOKIE_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PISAUTHENTICATED"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("AUTHENTICATEUSER");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PUSERID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PPASSWORD"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PISAUTHENTICATED"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CHKTOKENACCESS");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PTOKEN"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PACCESSKEY"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PACCESSKEYTYPE"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PHASACCESS"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DECODESTRING");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PINSTRING"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "POUTSTRING"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DECRYPTSTRING");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PINSTRING"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "POUTSTRING"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DELETEDOMAIN");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PDOMAINID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DELETEDOMAINMEMBERSHIP");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PGROUPID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PMEMBERSHIPID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DELETEGROUP");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PGROUPID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[9] = oper;

    }

    private static void _initOperationDesc2(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DELETEGROUPPACKAGE");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[10] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DELETEPACKAGEMEMBERSHIP");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PMEMBERSHIPID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PGROUPID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[11] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DELETEPAGEMEMBERSHIP");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PMEMBERSHIPID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PGROUPID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[12] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DELETEPORTALPAGE");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PPAGEID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PPAGETYPE"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "POWNERID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[13] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DELETEPORTALPAGEPORTLET");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PPAGEID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PPORTLETID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PPAGETYPE"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "POWNERID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[14] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DELETEPORTLET");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PPORTLETID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[15] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DELETEPORTLETCATEGORY");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[16] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DELETEPORTLETDOMAINMEMBERSHIP");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PDOMAINID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PPORTLETID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[17] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DELETEPORTLETMEMBERSHIP");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PMEMBERSHIPID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PGROUPID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[18] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DELETEUSER");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PUSERID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[19] = oper;

    }

    private static void _initOperationDesc3(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DELETEUSERMEMBERSHIP");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PMEMBERSHIPID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PGROUPID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[20] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ENCODESTRING");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PINSTRING"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "POUTSTRING"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[21] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ENCRYPTSTRING");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PINSTRING"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "POUTSTRING"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[22] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GETPAGENAVIGATION");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PUSERID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "POPTIONS"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPAGE_NAVIGATION", "ABSTRACTPAGE_NAVIGATIONArray"), ABSTRACTPAGE_NAVIGATION.services.applic.uniface.ABSTRACTPAGE_NAVIGATIONType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPAGE_NAVIGATION", "ABSTRACTPAGE_NAVIGATIONArray"), ABSTRACTPAGE_NAVIGATION.services.applic.uniface.ABSTRACTPAGE_NAVIGATIONType[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[23] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GETPORTALFOOTER");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "POPTIONS"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTFOOTER", "ABSTRACTFOOTERType"), ABSTRACTFOOTER.services.applic.uniface.ABSTRACTFOOTERType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTFOOTER_LINK", "ABSTRACTFOOTER_LINKArray"), ABSTRACTFOOTER_LINK.services.applic.uniface.ABSTRACTFOOTER_LINKType[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[24] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GETPORTALINFO");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PMENUBARFILTER"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PMENUFILTER"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PMENUITEMFILTER"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "POPTIONS"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_PARAM_CONFIG", "ABSTRACTPORTAL_PARAM_CONFIGArray"), ABSTRACTPORTAL_PARAM_CONFIG.services.applic.uniface.ABSTRACTPORTAL_PARAM_CONFIGType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_IMAGE_CONFIG", "ABSTRACTPORTAL_IMAGE_CONFIGArray"), ABSTRACTPORTAL_IMAGE_CONFIG.services.applic.uniface.ABSTRACTPORTAL_IMAGE_CONFIGType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_MESSAGE_CONFIG", "ABSTRACTPORTAL_MESSAGE_CONFIGArray"), ABSTRACTPORTAL_MESSAGE_CONFIG.services.applic.uniface.ABSTRACTPORTAL_MESSAGE_CONFIGType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_6"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTMENU_BAR", "ABSTRACTMENU_BARArray"), ABSTRACTMENU_BAR.services.applic.uniface.ABSTRACTMENU_BARType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_7"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTMENU", "ABSTRACTMENUArray"), ABSTRACTMENU.services.applic.uniface.ABSTRACTMENUType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_8"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTMENU_ITEM", "ABSTRACTMENU_ITEMArray"), ABSTRACTMENU_ITEM.services.applic.uniface.ABSTRACTMENU_ITEMType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_9"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTFOOTER", "ABSTRACTFOOTERType"), ABSTRACTFOOTER.services.applic.uniface.ABSTRACTFOOTERType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_10"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTFOOTER_LINK", "ABSTRACTFOOTER_LINKArray"), ABSTRACTFOOTER_LINK.services.applic.uniface.ABSTRACTFOOTER_LINKType[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[25] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GETPORTALMENU");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PMENUBARFILTER"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PMENUFILTER"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PMENUITEMFILTER"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "POPTIONS"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTMENU_BAR", "ABSTRACTMENU_BARArray"), ABSTRACTMENU_BAR.services.applic.uniface.ABSTRACTMENU_BARType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTMENU", "ABSTRACTMENUArray"), ABSTRACTMENU.services.applic.uniface.ABSTRACTMENUType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTMENU_ITEM", "ABSTRACTMENU_ITEMArray"), ABSTRACTMENU_ITEM.services.applic.uniface.ABSTRACTMENU_ITEMType[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[26] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GETPORTALPAGE");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PUSERID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PCURRENTPAGEID"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PCURRENTPAGETYPE"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "POPTIONS"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_PAGE_VIEW", "ABSTRACTPORTAL_PAGE_VIEWType"), ABSTRACTPORTAL_PAGE_VIEW.services.applic.uniface.ABSTRACTPORTAL_PAGE_VIEWType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_PAGE_PORTLET_VIEW", "ABSTRACTPORTAL_PAGE_PORTLET_VIEWArray"), ABSTRACTPORTAL_PAGE_PORTLET_VIEW.services.applic.uniface.ABSTRACTPORTAL_PAGE_PORTLET_VIEWType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPAGE_NAVIGATION", "ABSTRACTPAGE_NAVIGATIONArray"), ABSTRACTPAGE_NAVIGATION.services.applic.uniface.ABSTRACTPAGE_NAVIGATIONType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_6"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPAGE_NAVIGATION", "ABSTRACTPAGE_NAVIGATIONArray"), ABSTRACTPAGE_NAVIGATION.services.applic.uniface.ABSTRACTPAGE_NAVIGATIONType[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[27] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GETPORTLETCONTENT");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PPORTLETID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PUSERID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "POPTIONS"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PCONTENT"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[28] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GETUSERIDFROMTOKEN");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PTOKEN"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PUSERID"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PTIMESTAMP"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[29] = oper;

    }

    private static void _initOperationDesc4(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("INSERTDOMAIN");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_DOMAIN", "ABSTRACTPORTAL_DOMAINArray"), ABSTRACTPORTAL_DOMAIN.services.applic.uniface.ABSTRACTPORTAL_DOMAINType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_DOMAIN", "ABSTRACTPORTAL_DOMAINArray"), ABSTRACTPORTAL_DOMAIN.services.applic.uniface.ABSTRACTPORTAL_DOMAINType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[30] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("INSERTDOMAINMEMBERSHIP");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_MEMBERSHIP", "ABSTRACTGROUP_MEMBERSHIPArray"), ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[31] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("INSERTGROUP");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP", "ABSTRACTGROUPArray"), ABSTRACTGROUP.services.applic.uniface.ABSTRACTGROUPType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP", "ABSTRACTGROUPArray"), ABSTRACTGROUP.services.applic.uniface.ABSTRACTGROUPType[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[32] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("INSERTGROUPPACKAGE");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_PACKAGE", "ABSTRACTGROUP_PACKAGEArray"), ABSTRACTGROUP_PACKAGE.services.applic.uniface.ABSTRACTGROUP_PACKAGEType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_PACKAGE", "ABSTRACTGROUP_PACKAGEArray"), ABSTRACTGROUP_PACKAGE.services.applic.uniface.ABSTRACTGROUP_PACKAGEType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[33] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("INSERTPACKAGEMEMBERSHIP");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_MEMBERSHIP", "ABSTRACTGROUP_MEMBERSHIPArray"), ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[34] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("INSERTPAGEMEMBERSHIP");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_MEMBERSHIP", "ABSTRACTGROUP_MEMBERSHIPArray"), ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[35] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("INSERTPORTALPAGE");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_PAGE", "ABSTRACTPORTAL_PAGEArray"), ABSTRACTPORTAL_PAGE.services.applic.uniface.ABSTRACTPORTAL_PAGEType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_PAGE", "ABSTRACTPORTAL_PAGEArray"), ABSTRACTPORTAL_PAGE.services.applic.uniface.ABSTRACTPORTAL_PAGEType[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[36] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("INSERTPORTALPAGEPORTLET");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_PAGE_PORTLET", "ABSTRACTPORTAL_PAGE_PORTLETArray"), ABSTRACTPORTAL_PAGE_PORTLET.services.applic.uniface.ABSTRACTPORTAL_PAGE_PORTLETType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PPAGETYPE"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[37] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("INSERTPORTLETCATEGORY");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTLET_CATEGORY", "ABSTRACTPORTLET_CATEGORYArray"), ABSTRACTPORTLET_CATEGORY.services.applic.uniface.ABSTRACTPORTLET_CATEGORYType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTLET_CATEGORY", "ABSTRACTPORTLET_CATEGORYArray"), ABSTRACTPORTLET_CATEGORY.services.applic.uniface.ABSTRACTPORTLET_CATEGORYType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[38] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("INSERTPORTLETDOMAINMEMBERSHIP");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_MEMBERSHIP", "ABSTRACTGROUP_MEMBERSHIPArray"), ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[39] = oper;

    }

    private static void _initOperationDesc5(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("INSERTPORTLETMEMBERSHIP");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_MEMBERSHIP", "ABSTRACTGROUP_MEMBERSHIPArray"), ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[40] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("INSERTUSERMEMBERSHIP");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_USER_MEMBERSHIP", "ABSTRACTPORTAL_USER_MEMBERSHIPArray"), ABSTRACTPORTAL_USER_MEMBERSHIP.services.applic.uniface.ABSTRACTPORTAL_USER_MEMBERSHIPType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[41] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECTDOMAIN");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_DOMAIN", "ABSTRACTPORTAL_DOMAINType"), ABSTRACTPORTAL_DOMAIN.services.applic.uniface.ABSTRACTPORTAL_DOMAINType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_DOMAIN", "ABSTRACTPORTAL_DOMAINArray"), ABSTRACTPORTAL_DOMAIN.services.applic.uniface.ABSTRACTPORTAL_DOMAINType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[42] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECTDOMAINMEMBERSHIP");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_MEMBERSHIP", "ABSTRACTGROUP_MEMBERSHIPType"), ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_MEMBERSHIP", "ABSTRACTGROUP_MEMBERSHIPArray"), ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[43] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECTFILTERDOMAIN");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_DOMAIN", "ABSTRACTPORTAL_DOMAINType"), ABSTRACTPORTAL_DOMAIN.services.applic.uniface.ABSTRACTPORTAL_DOMAINType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_DOMAIN", "ABSTRACTPORTAL_DOMAINArray"), ABSTRACTPORTAL_DOMAIN.services.applic.uniface.ABSTRACTPORTAL_DOMAINType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[44] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECTFILTERGROUP");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP", "ABSTRACTGROUPType"), ABSTRACTGROUP.services.applic.uniface.ABSTRACTGROUPType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP", "ABSTRACTGROUPArray"), ABSTRACTGROUP.services.applic.uniface.ABSTRACTGROUPType[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[45] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECTFILTERGROUPPACKAGE");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_PACKAGE", "ABSTRACTGROUP_PACKAGEType"), ABSTRACTGROUP_PACKAGE.services.applic.uniface.ABSTRACTGROUP_PACKAGEType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_PACKAGE", "ABSTRACTGROUP_PACKAGEArray"), ABSTRACTGROUP_PACKAGE.services.applic.uniface.ABSTRACTGROUP_PACKAGEType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[46] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECTFILTERPACKAGEMEMBERSHIP");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_MEMBERSHIP", "ABSTRACTGROUP_MEMBERSHIPType"), ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_MEMBERSHIP", "ABSTRACTGROUP_MEMBERSHIPArray"), ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[47] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECTFILTERPORTALPAGE");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_PAGE", "ABSTRACTPORTAL_PAGEType"), ABSTRACTPORTAL_PAGE.services.applic.uniface.ABSTRACTPORTAL_PAGEType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_PAGE", "ABSTRACTPORTAL_PAGEArray"), ABSTRACTPORTAL_PAGE.services.applic.uniface.ABSTRACTPORTAL_PAGEType[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[48] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECTFILTERPORTALPORTLET");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTLET", "ABSTRACTPORTLETType"), ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTLET", "ABSTRACTPORTLETArray"), ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[49] = oper;

    }

    private static void _initOperationDesc6(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECTFILTERPORTLET");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTLET", "ABSTRACTPORTLETType"), ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTLET", "ABSTRACTPORTLETArray"), ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[50] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECTFILTERPORTLETBYUSER");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTLET", "ABSTRACTPORTLETType"), ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PUSERID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTLET", "ABSTRACTPORTLETArray"), ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[51] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECTFILTERPORTLETCATEGORY");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTLET_CATEGORY", "ABSTRACTPORTLET_CATEGORYType"), ABSTRACTPORTLET_CATEGORY.services.applic.uniface.ABSTRACTPORTLET_CATEGORYType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTLET_CATEGORY", "ABSTRACTPORTLET_CATEGORYArray"), ABSTRACTPORTLET_CATEGORY.services.applic.uniface.ABSTRACTPORTLET_CATEGORYType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[52] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECTFILTERUSER");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_USER", "ABSTRACTPORTAL_USERType"), ABSTRACTPORTAL_USER.services.applic.uniface.ABSTRACTPORTAL_USERType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_USER", "ABSTRACTPORTAL_USERArray"), ABSTRACTPORTAL_USER.services.applic.uniface.ABSTRACTPORTAL_USERType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[53] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECTGROUP");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP", "ABSTRACTGROUPType"), ABSTRACTGROUP.services.applic.uniface.ABSTRACTGROUPType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP", "ABSTRACTGROUPArray"), ABSTRACTGROUP.services.applic.uniface.ABSTRACTGROUPType[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[54] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECTGROUPPACKAGE");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_PACKAGE", "ABSTRACTGROUP_PACKAGEType"), ABSTRACTGROUP_PACKAGE.services.applic.uniface.ABSTRACTGROUP_PACKAGEType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_PACKAGE", "ABSTRACTGROUP_PACKAGEArray"), ABSTRACTGROUP_PACKAGE.services.applic.uniface.ABSTRACTGROUP_PACKAGEType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[55] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECTHTMLPORTLETCONTENT");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PUSERID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PPORTLETID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTHTML_PORTLET_PROPERTY", "ABSTRACTHTML_PORTLET_PROPERTYType"), ABSTRACTHTML_PORTLET_PROPERTY.services.applic.uniface.ABSTRACTHTML_PORTLET_PROPERTYType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[56] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECTJOBDETAIL");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTJOB_DETAIL", "ABSTRACTJOB_DETAILType"), ABSTRACTJOB_DETAIL.services.applic.uniface.ABSTRACTJOB_DETAILType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTJOB_DETAIL", "ABSTRACTJOB_DETAILArray"), ABSTRACTJOB_DETAIL.services.applic.uniface.ABSTRACTJOB_DETAILType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[57] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECTJOBTRIGGER");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTJOB_TRIGGER", "ABSTRACTJOB_TRIGGERType"), ABSTRACTJOB_TRIGGER.services.applic.uniface.ABSTRACTJOB_TRIGGERType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTJOB_TRIGGER", "ABSTRACTJOB_TRIGGERArray"), ABSTRACTJOB_TRIGGER.services.applic.uniface.ABSTRACTJOB_TRIGGERType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[58] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECTPACKAGEMEMBERSHIP");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_MEMBERSHIP", "ABSTRACTGROUP_MEMBERSHIPType"), ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_MEMBERSHIP", "ABSTRACTGROUP_MEMBERSHIPArray"), ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[59] = oper;

    }

    private static void _initOperationDesc7(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECTPAGEMEMBERSHIP");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_MEMBERSHIP", "ABSTRACTGROUP_MEMBERSHIPType"), ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_MEMBERSHIP", "ABSTRACTGROUP_MEMBERSHIPArray"), ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[60] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECTPORTALPAGE");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_PAGE", "ABSTRACTPORTAL_PAGEType"), ABSTRACTPORTAL_PAGE.services.applic.uniface.ABSTRACTPORTAL_PAGEType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_PAGE", "ABSTRACTPORTAL_PAGEArray"), ABSTRACTPORTAL_PAGE.services.applic.uniface.ABSTRACTPORTAL_PAGEType[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[61] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECTPORTALPAGEPORTLET");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_PAGE_PORTLET", "ABSTRACTPORTAL_PAGE_PORTLETType"), ABSTRACTPORTAL_PAGE_PORTLET.services.applic.uniface.ABSTRACTPORTAL_PAGE_PORTLETType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PPAGETYPE"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_PAGE_PORTLET", "ABSTRACTPORTAL_PAGE_PORTLETArray"), ABSTRACTPORTAL_PAGE_PORTLET.services.applic.uniface.ABSTRACTPORTAL_PAGE_PORTLETType[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[62] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECTPORTALPORTLET");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTLET", "ABSTRACTPORTLETType"), ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTLET", "ABSTRACTPORTLETArray"), ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[63] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECTPORTLET");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTLET", "ABSTRACTPORTLETType"), ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTLET", "ABSTRACTPORTLETArray"), ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[64] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECTPORTLETCATEGORY");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTLET_CATEGORY", "ABSTRACTPORTLET_CATEGORYType"), ABSTRACTPORTLET_CATEGORY.services.applic.uniface.ABSTRACTPORTLET_CATEGORYType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTLET_CATEGORY", "ABSTRACTPORTLET_CATEGORYArray"), ABSTRACTPORTLET_CATEGORY.services.applic.uniface.ABSTRACTPORTLET_CATEGORYType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[65] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECTPORTLETDOMAINMEMBERSHIP");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_MEMBERSHIP", "ABSTRACTGROUP_MEMBERSHIPType"), ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_MEMBERSHIP", "ABSTRACTGROUP_MEMBERSHIPArray"), ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[66] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECTPORTLETMEMBERSHIP");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_MEMBERSHIP", "ABSTRACTGROUP_MEMBERSHIPType"), ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_MEMBERSHIP", "ABSTRACTGROUP_MEMBERSHIPArray"), ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[67] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECTTEXTPORTLETCONTENT");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PUSERID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PPORTLETID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTHTML_PORTLET_PROPERTY", "ABSTRACTHTML_PORTLET_PROPERTYType"), ABSTRACTHTML_PORTLET_PROPERTY.services.applic.uniface.ABSTRACTHTML_PORTLET_PROPERTYType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[68] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECTUSER");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_USER", "ABSTRACTPORTAL_USERType"), ABSTRACTPORTAL_USER.services.applic.uniface.ABSTRACTPORTAL_USERType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_USER", "ABSTRACTPORTAL_USERArray"), ABSTRACTPORTAL_USER.services.applic.uniface.ABSTRACTPORTAL_USERType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[69] = oper;

    }

    private static void _initOperationDesc8(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SELECTUSERMEMBERSHIP");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_USER_MEMBERSHIP", "ABSTRACTPORTAL_USER_MEMBERSHIPType"), ABSTRACTPORTAL_USER_MEMBERSHIP.services.applic.uniface.ABSTRACTPORTAL_USER_MEMBERSHIPType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_USER_MEMBERSHIP", "ABSTRACTPORTAL_USER_MEMBERSHIPArray"), ABSTRACTPORTAL_USER_MEMBERSHIP.services.applic.uniface.ABSTRACTPORTAL_USER_MEMBERSHIPType[].class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[70] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("UPDATEDOMAIN");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_DOMAIN", "ABSTRACTPORTAL_DOMAINArray"), ABSTRACTPORTAL_DOMAIN.services.applic.uniface.ABSTRACTPORTAL_DOMAINType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[71] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("UPDATEGROUP");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP", "ABSTRACTGROUPArray"), ABSTRACTGROUP.services.applic.uniface.ABSTRACTGROUPType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[72] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("UPDATEGROUPPACKAGE");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_PACKAGE", "ABSTRACTGROUP_PACKAGEArray"), ABSTRACTGROUP_PACKAGE.services.applic.uniface.ABSTRACTGROUP_PACKAGEType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[73] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("UPDATEHTMLPORTLETCONTENT");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PUSERID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PPORTLETID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTHTML_PORTLET_PROPERTY", "ABSTRACTHTML_PORTLET_PROPERTYType"), ABSTRACTHTML_PORTLET_PROPERTY.services.applic.uniface.ABSTRACTHTML_PORTLET_PROPERTYType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[74] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("UPDATEPORTALPAGE");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_PAGE", "ABSTRACTPORTAL_PAGEArray"), ABSTRACTPORTAL_PAGE.services.applic.uniface.ABSTRACTPORTAL_PAGEType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[75] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("UPDATEPORTLET");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTLET", "ABSTRACTPORTLETArray"), ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[76] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("UPDATEPORTLETCATEGORY");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTLET_CATEGORY", "ABSTRACTPORTLET_CATEGORYArray"), ABSTRACTPORTLET_CATEGORY.services.applic.uniface.ABSTRACTPORTLET_CATEGORYType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[77] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("UPDATETEXTPORTLETCONTENT");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PUSERID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PPORTLETID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTHTML_PORTLET_PROPERTY", "ABSTRACTHTML_PORTLET_PROPERTYType"), ABSTRACTHTML_PORTLET_PROPERTY.services.applic.uniface.ABSTRACTHTML_PORTLET_PROPERTYType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[78] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("UPDATEUSER");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_USER", "ABSTRACTPORTAL_USERArray"), ABSTRACTPORTAL_USER.services.applic.uniface.ABSTRACTPORTAL_USERType[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[79] = oper;

    }

    private static void _initOperationDesc9(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("UPDATEUSERPASSWORD");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PUSERID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PPASSWORD"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[80] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("VALIDATEPORTALUSER");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDCOOKIE_CONTEXT", "STANDARDCOOKIE_CONTEXTType"), STANDARDCOOKIE_CONTEXT.services.applic.uniface.STANDARDCOOKIE_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PUSERID"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDCOOKIE_CONTEXT", "STANDARDCOOKIE_CONTEXTType"), STANDARDCOOKIE_CONTEXT.services.applic.uniface.STANDARDCOOKIE_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PISVALIDATED"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[81] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("VALIDATEUSER");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType"), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "return"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType"), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "PISVALIDATED"), org.apache.axis.description.ParameterDesc.OUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[82] = oper;

    }

    public UVCONNECTORBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public UVCONNECTORBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public UVCONNECTORBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTFOOTER_LINK", "ABSTRACTFOOTER_LINKArray");
            cachedSerQNames.add(qName);
            cls = ABSTRACTFOOTER_LINK.services.applic.uniface.ABSTRACTFOOTER_LINKType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTFOOTER_LINK", "ABSTRACTFOOTER_LINKType");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTFOOTER_LINK", "ABSTRACTFOOTER_LINKType");
            cachedSerQNames.add(qName);
            cls = ABSTRACTFOOTER_LINK.services.applic.uniface.ABSTRACTFOOTER_LINKType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTFOOTER", "ABSTRACTFOOTERType");
            cachedSerQNames.add(qName);
            cls = ABSTRACTFOOTER.services.applic.uniface.ABSTRACTFOOTERType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_MEMBERSHIP", "ABSTRACTGROUP_MEMBERSHIPArray");
            cachedSerQNames.add(qName);
            cls = ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_MEMBERSHIP", "ABSTRACTGROUP_MEMBERSHIPType");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_MEMBERSHIP", "ABSTRACTGROUP_MEMBERSHIPType");
            cachedSerQNames.add(qName);
            cls = ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_PACKAGE", "ABSTRACTGROUP_PACKAGEArray");
            cachedSerQNames.add(qName);
            cls = ABSTRACTGROUP_PACKAGE.services.applic.uniface.ABSTRACTGROUP_PACKAGEType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_PACKAGE", "ABSTRACTGROUP_PACKAGEType");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP_PACKAGE", "ABSTRACTGROUP_PACKAGEType");
            cachedSerQNames.add(qName);
            cls = ABSTRACTGROUP_PACKAGE.services.applic.uniface.ABSTRACTGROUP_PACKAGEType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP", "ABSTRACTGROUPArray");
            cachedSerQNames.add(qName);
            cls = ABSTRACTGROUP.services.applic.uniface.ABSTRACTGROUPType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP", "ABSTRACTGROUPType");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTGROUP", "ABSTRACTGROUPType");
            cachedSerQNames.add(qName);
            cls = ABSTRACTGROUP.services.applic.uniface.ABSTRACTGROUPType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTHTML_PORTLET_PROPERTY", "ABSTRACTHTML_PORTLET_PROPERTYType");
            cachedSerQNames.add(qName);
            cls = ABSTRACTHTML_PORTLET_PROPERTY.services.applic.uniface.ABSTRACTHTML_PORTLET_PROPERTYType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTJOB_DETAIL", "ABSTRACTJOB_DETAILArray");
            cachedSerQNames.add(qName);
            cls = ABSTRACTJOB_DETAIL.services.applic.uniface.ABSTRACTJOB_DETAILType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTJOB_DETAIL", "ABSTRACTJOB_DETAILType");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTJOB_DETAIL", "ABSTRACTJOB_DETAILType");
            cachedSerQNames.add(qName);
            cls = ABSTRACTJOB_DETAIL.services.applic.uniface.ABSTRACTJOB_DETAILType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTJOB_TRIGGER", "ABSTRACTJOB_TRIGGERArray");
            cachedSerQNames.add(qName);
            cls = ABSTRACTJOB_TRIGGER.services.applic.uniface.ABSTRACTJOB_TRIGGERType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTJOB_TRIGGER", "ABSTRACTJOB_TRIGGERType");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTJOB_TRIGGER", "ABSTRACTJOB_TRIGGERType");
            cachedSerQNames.add(qName);
            cls = ABSTRACTJOB_TRIGGER.services.applic.uniface.ABSTRACTJOB_TRIGGERType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTMENU_BAR", "ABSTRACTMENU_BARArray");
            cachedSerQNames.add(qName);
            cls = ABSTRACTMENU_BAR.services.applic.uniface.ABSTRACTMENU_BARType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTMENU_BAR", "ABSTRACTMENU_BARType");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTMENU_BAR", "ABSTRACTMENU_BARType");
            cachedSerQNames.add(qName);
            cls = ABSTRACTMENU_BAR.services.applic.uniface.ABSTRACTMENU_BARType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTMENU_ITEM", "ABSTRACTMENU_ITEMArray");
            cachedSerQNames.add(qName);
            cls = ABSTRACTMENU_ITEM.services.applic.uniface.ABSTRACTMENU_ITEMType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTMENU_ITEM", "ABSTRACTMENU_ITEMType");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTMENU_ITEM", "ABSTRACTMENU_ITEMType");
            cachedSerQNames.add(qName);
            cls = ABSTRACTMENU_ITEM.services.applic.uniface.ABSTRACTMENU_ITEMType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTMENU", "ABSTRACTMENUArray");
            cachedSerQNames.add(qName);
            cls = ABSTRACTMENU.services.applic.uniface.ABSTRACTMENUType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTMENU", "ABSTRACTMENUType");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTMENU", "ABSTRACTMENUType");
            cachedSerQNames.add(qName);
            cls = ABSTRACTMENU.services.applic.uniface.ABSTRACTMENUType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPAGE_NAVIGATION", "ABSTRACTPAGE_NAVIGATIONArray");
            cachedSerQNames.add(qName);
            cls = ABSTRACTPAGE_NAVIGATION.services.applic.uniface.ABSTRACTPAGE_NAVIGATIONType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPAGE_NAVIGATION", "ABSTRACTPAGE_NAVIGATIONType");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPAGE_NAVIGATION", "ABSTRACTPAGE_NAVIGATIONType");
            cachedSerQNames.add(qName);
            cls = ABSTRACTPAGE_NAVIGATION.services.applic.uniface.ABSTRACTPAGE_NAVIGATIONType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_DOMAIN", "ABSTRACTPORTAL_DOMAINArray");
            cachedSerQNames.add(qName);
            cls = ABSTRACTPORTAL_DOMAIN.services.applic.uniface.ABSTRACTPORTAL_DOMAINType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_DOMAIN", "ABSTRACTPORTAL_DOMAINType");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_DOMAIN", "ABSTRACTPORTAL_DOMAINType");
            cachedSerQNames.add(qName);
            cls = ABSTRACTPORTAL_DOMAIN.services.applic.uniface.ABSTRACTPORTAL_DOMAINType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_IMAGE_CONFIG", "ABSTRACTPORTAL_IMAGE_CONFIGArray");
            cachedSerQNames.add(qName);
            cls = ABSTRACTPORTAL_IMAGE_CONFIG.services.applic.uniface.ABSTRACTPORTAL_IMAGE_CONFIGType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_IMAGE_CONFIG", "ABSTRACTPORTAL_IMAGE_CONFIGType");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_IMAGE_CONFIG", "ABSTRACTPORTAL_IMAGE_CONFIGType");
            cachedSerQNames.add(qName);
            cls = ABSTRACTPORTAL_IMAGE_CONFIG.services.applic.uniface.ABSTRACTPORTAL_IMAGE_CONFIGType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_MESSAGE_CONFIG", "ABSTRACTPORTAL_MESSAGE_CONFIGArray");
            cachedSerQNames.add(qName);
            cls = ABSTRACTPORTAL_MESSAGE_CONFIG.services.applic.uniface.ABSTRACTPORTAL_MESSAGE_CONFIGType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_MESSAGE_CONFIG", "ABSTRACTPORTAL_MESSAGE_CONFIGType");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_MESSAGE_CONFIG", "ABSTRACTPORTAL_MESSAGE_CONFIGType");
            cachedSerQNames.add(qName);
            cls = ABSTRACTPORTAL_MESSAGE_CONFIG.services.applic.uniface.ABSTRACTPORTAL_MESSAGE_CONFIGType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_PAGE_PORTLET_VIEW", "ABSTRACTPORTAL_PAGE_PORTLET_VIEWArray");
            cachedSerQNames.add(qName);
            cls = ABSTRACTPORTAL_PAGE_PORTLET_VIEW.services.applic.uniface.ABSTRACTPORTAL_PAGE_PORTLET_VIEWType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_PAGE_PORTLET_VIEW", "ABSTRACTPORTAL_PAGE_PORTLET_VIEWType");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_PAGE_PORTLET_VIEW", "ABSTRACTPORTAL_PAGE_PORTLET_VIEWType");
            cachedSerQNames.add(qName);
            cls = ABSTRACTPORTAL_PAGE_PORTLET_VIEW.services.applic.uniface.ABSTRACTPORTAL_PAGE_PORTLET_VIEWType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_PAGE_PORTLET", "ABSTRACTPORTAL_PAGE_PORTLETArray");
            cachedSerQNames.add(qName);
            cls = ABSTRACTPORTAL_PAGE_PORTLET.services.applic.uniface.ABSTRACTPORTAL_PAGE_PORTLETType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_PAGE_PORTLET", "ABSTRACTPORTAL_PAGE_PORTLETType");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_PAGE_PORTLET", "ABSTRACTPORTAL_PAGE_PORTLETType");
            cachedSerQNames.add(qName);
            cls = ABSTRACTPORTAL_PAGE_PORTLET.services.applic.uniface.ABSTRACTPORTAL_PAGE_PORTLETType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_PAGE_VIEW", "ABSTRACTPORTAL_PAGE_VIEWType");
            cachedSerQNames.add(qName);
            cls = ABSTRACTPORTAL_PAGE_VIEW.services.applic.uniface.ABSTRACTPORTAL_PAGE_VIEWType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_PAGE", "ABSTRACTPORTAL_PAGEArray");
            cachedSerQNames.add(qName);
            cls = ABSTRACTPORTAL_PAGE.services.applic.uniface.ABSTRACTPORTAL_PAGEType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_PAGE", "ABSTRACTPORTAL_PAGEType");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_PAGE", "ABSTRACTPORTAL_PAGEType");
            cachedSerQNames.add(qName);
            cls = ABSTRACTPORTAL_PAGE.services.applic.uniface.ABSTRACTPORTAL_PAGEType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_PARAM_CONFIG", "ABSTRACTPORTAL_PARAM_CONFIGArray");
            cachedSerQNames.add(qName);
            cls = ABSTRACTPORTAL_PARAM_CONFIG.services.applic.uniface.ABSTRACTPORTAL_PARAM_CONFIGType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_PARAM_CONFIG", "ABSTRACTPORTAL_PARAM_CONFIGType");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_PARAM_CONFIG", "ABSTRACTPORTAL_PARAM_CONFIGType");
            cachedSerQNames.add(qName);
            cls = ABSTRACTPORTAL_PARAM_CONFIG.services.applic.uniface.ABSTRACTPORTAL_PARAM_CONFIGType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_USER_MEMBERSHIP", "ABSTRACTPORTAL_USER_MEMBERSHIPArray");
            cachedSerQNames.add(qName);
            cls = ABSTRACTPORTAL_USER_MEMBERSHIP.services.applic.uniface.ABSTRACTPORTAL_USER_MEMBERSHIPType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_USER_MEMBERSHIP", "ABSTRACTPORTAL_USER_MEMBERSHIPType");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_USER_MEMBERSHIP", "ABSTRACTPORTAL_USER_MEMBERSHIPType");
            cachedSerQNames.add(qName);
            cls = ABSTRACTPORTAL_USER_MEMBERSHIP.services.applic.uniface.ABSTRACTPORTAL_USER_MEMBERSHIPType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_USER", "ABSTRACTPORTAL_USERArray");
            cachedSerQNames.add(qName);
            cls = ABSTRACTPORTAL_USER.services.applic.uniface.ABSTRACTPORTAL_USERType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_USER", "ABSTRACTPORTAL_USERType");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTAL_USER", "ABSTRACTPORTAL_USERType");
            cachedSerQNames.add(qName);
            cls = ABSTRACTPORTAL_USER.services.applic.uniface.ABSTRACTPORTAL_USERType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTLET_CATEGORY", "ABSTRACTPORTLET_CATEGORYArray");
            cachedSerQNames.add(qName);
            cls = ABSTRACTPORTLET_CATEGORY.services.applic.uniface.ABSTRACTPORTLET_CATEGORYType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTLET_CATEGORY", "ABSTRACTPORTLET_CATEGORYType");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTLET_CATEGORY", "ABSTRACTPORTLET_CATEGORYType");
            cachedSerQNames.add(qName);
            cls = ABSTRACTPORTLET_CATEGORY.services.applic.uniface.ABSTRACTPORTLET_CATEGORYType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTLET", "ABSTRACTPORTLETArray");
            cachedSerQNames.add(qName);
            cls = ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTLET", "ABSTRACTPORTLETType");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:ABSTRACTPORTLET", "ABSTRACTPORTLETType");
            cachedSerQNames.add(qName);
            cls = ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDCOOKIE_CONTEXT", "STANDARDCOOKIE_CONTEXTType");
            cachedSerQNames.add(qName);
            cls = STANDARDCOOKIE_CONTEXT.services.applic.uniface.STANDARDCOOKIE_CONTEXTType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSECURITY_CONTEXT", "STANDARDSECURITY_CONTEXTType");
            cachedSerQNames.add(qName);
            cls = STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:uniface:applic:services:STANDARDSTATUS_CONTEXT", "STANDARDSTATUS_CONTEXTType");
            cachedSerQNames.add(qName);
            cls = STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
                    _call.setEncodingStyle(org.apache.axis.Constants.URI_SOAP11_ENC);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public void ADDPORTALPORTLET(ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType[] GENERATED_PARAMETER_NAME_1, java.lang.String PPACKAGEID, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, ABSTRACTPORTLET.services.applic.uniface.holders.ABSTRACTPORTLETArrayHolder GENERATED_PARAMETER_NAME_4, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_5) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "ADDPORTALPORTLET"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, PPACKAGEID, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_5.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_5.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void ADDPORTALUSER(ABSTRACTPORTAL_USER.services.applic.uniface.ABSTRACTPORTAL_USERType[] GENERATED_PARAMETER_NAME_1, java.lang.String PPACKAGEID, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "ADDPORTALUSER"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, PPACKAGEID, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void AUTHENTICATEPORTALUSER(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, java.lang.String PUSERID, java.lang.String PPASSWORD, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2, STANDARDCOOKIE_CONTEXT.services.applic.uniface.holders.STANDARDCOOKIE_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.BooleanHolder PISAUTHENTICATED) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "AUTHENTICATEPORTALUSER"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, PUSERID, PPASSWORD});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
            try {
                GENERATED_PARAMETER_NAME_3.value = (STANDARDCOOKIE_CONTEXT.services.applic.uniface.STANDARDCOOKIE_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_3.value = (STANDARDCOOKIE_CONTEXT.services.applic.uniface.STANDARDCOOKIE_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3")), STANDARDCOOKIE_CONTEXT.services.applic.uniface.STANDARDCOOKIE_CONTEXTType.class);
            }
            try {
                PISAUTHENTICATED.value = ((java.lang.Boolean) _output.get(new javax.xml.namespace.QName("", "PISAUTHENTICATED"))).booleanValue();
            } catch (java.lang.Exception _exception) {
                PISAUTHENTICATED.value = ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "PISAUTHENTICATED")), boolean.class)).booleanValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void AUTHENTICATEUSER(java.lang.String PUSERID, java.lang.String PPASSWORD, javax.xml.rpc.holders.IntHolder _return, STANDARDSECURITY_CONTEXT.services.applic.uniface.holders.STANDARDSECURITY_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_1, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2, javax.xml.rpc.holders.BooleanHolder PISAUTHENTICATED) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "AUTHENTICATEUSER"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {PUSERID, PPASSWORD});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_1.value = (STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_1.value = (STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_1")), STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType.class);
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
            try {
                PISAUTHENTICATED.value = ((java.lang.Boolean) _output.get(new javax.xml.namespace.QName("", "PISAUTHENTICATED"))).booleanValue();
            } catch (java.lang.Exception _exception) {
                PISAUTHENTICATED.value = ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "PISAUTHENTICATED")), boolean.class)).booleanValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void CHKTOKENACCESS(java.lang.String PTOKEN, java.lang.String PACCESSKEY, java.lang.String PACCESSKEYTYPE, javax.xml.rpc.holders.IntHolder _return, javax.xml.rpc.holders.StringHolder PHASACCESS) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "CHKTOKENACCESS"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {PTOKEN, PACCESSKEY, PACCESSKEYTYPE});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                PHASACCESS.value = (java.lang.String) _output.get(new javax.xml.namespace.QName("", "PHASACCESS"));
            } catch (java.lang.Exception _exception) {
                PHASACCESS.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "PHASACCESS")), java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void DECODESTRING(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, java.lang.String PINSTRING, javax.xml.rpc.holders.IntHolder _return, javax.xml.rpc.holders.StringHolder POUTSTRING) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "DECODESTRING"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, PINSTRING});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                POUTSTRING.value = (java.lang.String) _output.get(new javax.xml.namespace.QName("", "POUTSTRING"));
            } catch (java.lang.Exception _exception) {
                POUTSTRING.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "POUTSTRING")), java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void DECRYPTSTRING(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, java.lang.String PINSTRING, javax.xml.rpc.holders.IntHolder _return, javax.xml.rpc.holders.StringHolder POUTSTRING) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "DECRYPTSTRING"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, PINSTRING});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                POUTSTRING.value = (java.lang.String) _output.get(new javax.xml.namespace.QName("", "POUTSTRING"));
            } catch (java.lang.Exception _exception) {
                POUTSTRING.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "POUTSTRING")), java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void DELETEDOMAIN(java.lang.String PDOMAINID, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_2, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_3) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "DELETEDOMAIN"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {PDOMAINID, GENERATED_PARAMETER_NAME_2});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_3.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_3.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void DELETEDOMAINMEMBERSHIP(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, java.lang.String PGROUPID, java.lang.String PMEMBERSHIPID, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "DELETEDOMAINMEMBERSHIP"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, PGROUPID, PMEMBERSHIPID});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void DELETEGROUP(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, java.lang.String PGROUPID, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "DELETEGROUP"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, PGROUPID});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void DELETEGROUPPACKAGE(java.lang.String PID, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_2, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_3) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "DELETEGROUPPACKAGE"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {PID, GENERATED_PARAMETER_NAME_2});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_3.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_3.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void DELETEPACKAGEMEMBERSHIP(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, java.lang.String PMEMBERSHIPID, java.lang.String PGROUPID, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[11]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "DELETEPACKAGEMEMBERSHIP"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, PMEMBERSHIPID, PGROUPID});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void DELETEPAGEMEMBERSHIP(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, java.lang.String PMEMBERSHIPID, java.lang.String PGROUPID, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[12]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "DELETEPAGEMEMBERSHIP"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, PMEMBERSHIPID, PGROUPID});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void DELETEPORTALPAGE(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, java.lang.String PPAGEID, java.lang.String PPAGETYPE, java.lang.String POWNERID, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[13]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "DELETEPORTALPAGE"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, PPAGEID, PPAGETYPE, POWNERID});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void DELETEPORTALPAGEPORTLET(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, java.lang.String PPAGEID, java.lang.String PPORTLETID, java.lang.String PPAGETYPE, java.lang.String POWNERID, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[14]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "DELETEPORTALPAGEPORTLET"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, PPAGEID, PPORTLETID, PPAGETYPE, POWNERID});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void DELETEPORTLET(java.lang.String PPORTLETID, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_2, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_3) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[15]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "DELETEPORTLET"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {PPORTLETID, GENERATED_PARAMETER_NAME_2});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_3.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_3.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void DELETEPORTLETCATEGORY(java.lang.String PID, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_2, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_3) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[16]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "DELETEPORTLETCATEGORY"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {PID, GENERATED_PARAMETER_NAME_2});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_3.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_3.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void DELETEPORTLETDOMAINMEMBERSHIP(java.lang.String PDOMAINID, java.lang.String PPORTLETID, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[17]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "DELETEPORTLETDOMAINMEMBERSHIP"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {PDOMAINID, PPORTLETID, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void DELETEPORTLETMEMBERSHIP(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, java.lang.String PMEMBERSHIPID, java.lang.String PGROUPID, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[18]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "DELETEPORTLETMEMBERSHIP"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, PMEMBERSHIPID, PGROUPID});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void DELETEUSER(java.lang.String PUSERID, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_2, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_3) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[19]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "DELETEUSER"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {PUSERID, GENERATED_PARAMETER_NAME_2});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_3.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_3.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void DELETEUSERMEMBERSHIP(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, java.lang.String PMEMBERSHIPID, java.lang.String PGROUPID, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[20]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "DELETEUSERMEMBERSHIP"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, PMEMBERSHIPID, PGROUPID});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void ENCODESTRING(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, java.lang.String PINSTRING, javax.xml.rpc.holders.IntHolder _return, javax.xml.rpc.holders.StringHolder POUTSTRING) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[21]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "ENCODESTRING"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, PINSTRING});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                POUTSTRING.value = (java.lang.String) _output.get(new javax.xml.namespace.QName("", "POUTSTRING"));
            } catch (java.lang.Exception _exception) {
                POUTSTRING.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "POUTSTRING")), java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void ENCRYPTSTRING(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, java.lang.String PINSTRING, javax.xml.rpc.holders.IntHolder _return, javax.xml.rpc.holders.StringHolder POUTSTRING) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[22]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "ENCRYPTSTRING"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, PINSTRING});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                POUTSTRING.value = (java.lang.String) _output.get(new javax.xml.namespace.QName("", "POUTSTRING"));
            } catch (java.lang.Exception _exception) {
                POUTSTRING.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "POUTSTRING")), java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void GETPAGENAVIGATION(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, java.lang.String PUSERID, java.lang.String POPTIONS, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2, ABSTRACTPAGE_NAVIGATION.services.applic.uniface.holders.ABSTRACTPAGE_NAVIGATIONArrayHolder GENERATED_PARAMETER_NAME_3, ABSTRACTPAGE_NAVIGATION.services.applic.uniface.holders.ABSTRACTPAGE_NAVIGATIONArrayHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[23]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "GETPAGENAVIGATION"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, PUSERID, POPTIONS});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
            try {
                GENERATED_PARAMETER_NAME_3.value = (ABSTRACTPAGE_NAVIGATION.services.applic.uniface.ABSTRACTPAGE_NAVIGATIONType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_3.value = (ABSTRACTPAGE_NAVIGATION.services.applic.uniface.ABSTRACTPAGE_NAVIGATIONType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3")), ABSTRACTPAGE_NAVIGATION.services.applic.uniface.ABSTRACTPAGE_NAVIGATIONType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTPAGE_NAVIGATION.services.applic.uniface.ABSTRACTPAGE_NAVIGATIONType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTPAGE_NAVIGATION.services.applic.uniface.ABSTRACTPAGE_NAVIGATIONType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), ABSTRACTPAGE_NAVIGATION.services.applic.uniface.ABSTRACTPAGE_NAVIGATIONType[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void GETPORTALFOOTER(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, java.lang.String POPTIONS, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2, ABSTRACTFOOTER.services.applic.uniface.holders.ABSTRACTFOOTERTypeHolder GENERATED_PARAMETER_NAME_3, ABSTRACTFOOTER_LINK.services.applic.uniface.holders.ABSTRACTFOOTER_LINKArrayHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[24]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "GETPORTALFOOTER"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, POPTIONS});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
            try {
                GENERATED_PARAMETER_NAME_3.value = (ABSTRACTFOOTER.services.applic.uniface.ABSTRACTFOOTERType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_3.value = (ABSTRACTFOOTER.services.applic.uniface.ABSTRACTFOOTERType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3")), ABSTRACTFOOTER.services.applic.uniface.ABSTRACTFOOTERType.class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTFOOTER_LINK.services.applic.uniface.ABSTRACTFOOTER_LINKType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTFOOTER_LINK.services.applic.uniface.ABSTRACTFOOTER_LINKType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), ABSTRACTFOOTER_LINK.services.applic.uniface.ABSTRACTFOOTER_LINKType[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void GETPORTALINFO(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, java.lang.String PMENUBARFILTER, java.lang.String PMENUFILTER, java.lang.String PMENUITEMFILTER, java.lang.String POPTIONS, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2, ABSTRACTPORTAL_PARAM_CONFIG.services.applic.uniface.holders.ABSTRACTPORTAL_PARAM_CONFIGArrayHolder GENERATED_PARAMETER_NAME_3, ABSTRACTPORTAL_IMAGE_CONFIG.services.applic.uniface.holders.ABSTRACTPORTAL_IMAGE_CONFIGArrayHolder GENERATED_PARAMETER_NAME_4, ABSTRACTPORTAL_MESSAGE_CONFIG.services.applic.uniface.holders.ABSTRACTPORTAL_MESSAGE_CONFIGArrayHolder GENERATED_PARAMETER_NAME_5, ABSTRACTMENU_BAR.services.applic.uniface.holders.ABSTRACTMENU_BARArrayHolder GENERATED_PARAMETER_NAME_6, ABSTRACTMENU.services.applic.uniface.holders.ABSTRACTMENUArrayHolder GENERATED_PARAMETER_NAME_7, ABSTRACTMENU_ITEM.services.applic.uniface.holders.ABSTRACTMENU_ITEMArrayHolder GENERATED_PARAMETER_NAME_8, ABSTRACTFOOTER.services.applic.uniface.holders.ABSTRACTFOOTERTypeHolder GENERATED_PARAMETER_NAME_9, ABSTRACTFOOTER_LINK.services.applic.uniface.holders.ABSTRACTFOOTER_LINKArrayHolder GENERATED_PARAMETER_NAME_10) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[25]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "GETPORTALINFO"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, PMENUBARFILTER, PMENUFILTER, PMENUITEMFILTER, POPTIONS});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
            try {
                GENERATED_PARAMETER_NAME_3.value = (ABSTRACTPORTAL_PARAM_CONFIG.services.applic.uniface.ABSTRACTPORTAL_PARAM_CONFIGType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_3.value = (ABSTRACTPORTAL_PARAM_CONFIG.services.applic.uniface.ABSTRACTPORTAL_PARAM_CONFIGType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3")), ABSTRACTPORTAL_PARAM_CONFIG.services.applic.uniface.ABSTRACTPORTAL_PARAM_CONFIGType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTPORTAL_IMAGE_CONFIG.services.applic.uniface.ABSTRACTPORTAL_IMAGE_CONFIGType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTPORTAL_IMAGE_CONFIG.services.applic.uniface.ABSTRACTPORTAL_IMAGE_CONFIGType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), ABSTRACTPORTAL_IMAGE_CONFIG.services.applic.uniface.ABSTRACTPORTAL_IMAGE_CONFIGType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_5.value = (ABSTRACTPORTAL_MESSAGE_CONFIG.services.applic.uniface.ABSTRACTPORTAL_MESSAGE_CONFIGType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_5.value = (ABSTRACTPORTAL_MESSAGE_CONFIG.services.applic.uniface.ABSTRACTPORTAL_MESSAGE_CONFIGType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5")), ABSTRACTPORTAL_MESSAGE_CONFIG.services.applic.uniface.ABSTRACTPORTAL_MESSAGE_CONFIGType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_6.value = (ABSTRACTMENU_BAR.services.applic.uniface.ABSTRACTMENU_BARType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_6"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_6.value = (ABSTRACTMENU_BAR.services.applic.uniface.ABSTRACTMENU_BARType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_6")), ABSTRACTMENU_BAR.services.applic.uniface.ABSTRACTMENU_BARType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_7.value = (ABSTRACTMENU.services.applic.uniface.ABSTRACTMENUType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_7"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_7.value = (ABSTRACTMENU.services.applic.uniface.ABSTRACTMENUType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_7")), ABSTRACTMENU.services.applic.uniface.ABSTRACTMENUType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_8.value = (ABSTRACTMENU_ITEM.services.applic.uniface.ABSTRACTMENU_ITEMType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_8"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_8.value = (ABSTRACTMENU_ITEM.services.applic.uniface.ABSTRACTMENU_ITEMType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_8")), ABSTRACTMENU_ITEM.services.applic.uniface.ABSTRACTMENU_ITEMType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_9.value = (ABSTRACTFOOTER.services.applic.uniface.ABSTRACTFOOTERType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_9"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_9.value = (ABSTRACTFOOTER.services.applic.uniface.ABSTRACTFOOTERType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_9")), ABSTRACTFOOTER.services.applic.uniface.ABSTRACTFOOTERType.class);
            }
            try {
                GENERATED_PARAMETER_NAME_10.value = (ABSTRACTFOOTER_LINK.services.applic.uniface.ABSTRACTFOOTER_LINKType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_10"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_10.value = (ABSTRACTFOOTER_LINK.services.applic.uniface.ABSTRACTFOOTER_LINKType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_10")), ABSTRACTFOOTER_LINK.services.applic.uniface.ABSTRACTFOOTER_LINKType[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void GETPORTALMENU(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, java.lang.String PMENUBARFILTER, java.lang.String PMENUFILTER, java.lang.String PMENUITEMFILTER, java.lang.String POPTIONS, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2, ABSTRACTMENU_BAR.services.applic.uniface.holders.ABSTRACTMENU_BARArrayHolder GENERATED_PARAMETER_NAME_3, ABSTRACTMENU.services.applic.uniface.holders.ABSTRACTMENUArrayHolder GENERATED_PARAMETER_NAME_4, ABSTRACTMENU_ITEM.services.applic.uniface.holders.ABSTRACTMENU_ITEMArrayHolder GENERATED_PARAMETER_NAME_5) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[26]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "GETPORTALMENU"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, PMENUBARFILTER, PMENUFILTER, PMENUITEMFILTER, POPTIONS});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
            try {
                GENERATED_PARAMETER_NAME_3.value = (ABSTRACTMENU_BAR.services.applic.uniface.ABSTRACTMENU_BARType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_3.value = (ABSTRACTMENU_BAR.services.applic.uniface.ABSTRACTMENU_BARType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3")), ABSTRACTMENU_BAR.services.applic.uniface.ABSTRACTMENU_BARType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTMENU.services.applic.uniface.ABSTRACTMENUType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTMENU.services.applic.uniface.ABSTRACTMENUType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), ABSTRACTMENU.services.applic.uniface.ABSTRACTMENUType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_5.value = (ABSTRACTMENU_ITEM.services.applic.uniface.ABSTRACTMENU_ITEMType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_5.value = (ABSTRACTMENU_ITEM.services.applic.uniface.ABSTRACTMENU_ITEMType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5")), ABSTRACTMENU_ITEM.services.applic.uniface.ABSTRACTMENU_ITEMType[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void GETPORTALPAGE(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, java.lang.String PUSERID, javax.xml.rpc.holders.StringHolder PCURRENTPAGEID, javax.xml.rpc.holders.StringHolder PCURRENTPAGETYPE, java.lang.String POPTIONS, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2, ABSTRACTPORTAL_PAGE_VIEW.services.applic.uniface.holders.ABSTRACTPORTAL_PAGE_VIEWTypeHolder GENERATED_PARAMETER_NAME_3, ABSTRACTPORTAL_PAGE_PORTLET_VIEW.services.applic.uniface.holders.ABSTRACTPORTAL_PAGE_PORTLET_VIEWArrayHolder GENERATED_PARAMETER_NAME_4, ABSTRACTPAGE_NAVIGATION.services.applic.uniface.holders.ABSTRACTPAGE_NAVIGATIONArrayHolder GENERATED_PARAMETER_NAME_5, ABSTRACTPAGE_NAVIGATION.services.applic.uniface.holders.ABSTRACTPAGE_NAVIGATIONArrayHolder GENERATED_PARAMETER_NAME_6) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[27]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "GETPORTALPAGE"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, PUSERID, PCURRENTPAGEID.value, PCURRENTPAGETYPE.value, POPTIONS});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                PCURRENTPAGEID.value = (java.lang.String) _output.get(new javax.xml.namespace.QName("", "PCURRENTPAGEID"));
            } catch (java.lang.Exception _exception) {
                PCURRENTPAGEID.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "PCURRENTPAGEID")), java.lang.String.class);
            }
            try {
                PCURRENTPAGETYPE.value = (java.lang.String) _output.get(new javax.xml.namespace.QName("", "PCURRENTPAGETYPE"));
            } catch (java.lang.Exception _exception) {
                PCURRENTPAGETYPE.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "PCURRENTPAGETYPE")), java.lang.String.class);
            }
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
            try {
                GENERATED_PARAMETER_NAME_3.value = (ABSTRACTPORTAL_PAGE_VIEW.services.applic.uniface.ABSTRACTPORTAL_PAGE_VIEWType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_3.value = (ABSTRACTPORTAL_PAGE_VIEW.services.applic.uniface.ABSTRACTPORTAL_PAGE_VIEWType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3")), ABSTRACTPORTAL_PAGE_VIEW.services.applic.uniface.ABSTRACTPORTAL_PAGE_VIEWType.class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTPORTAL_PAGE_PORTLET_VIEW.services.applic.uniface.ABSTRACTPORTAL_PAGE_PORTLET_VIEWType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTPORTAL_PAGE_PORTLET_VIEW.services.applic.uniface.ABSTRACTPORTAL_PAGE_PORTLET_VIEWType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), ABSTRACTPORTAL_PAGE_PORTLET_VIEW.services.applic.uniface.ABSTRACTPORTAL_PAGE_PORTLET_VIEWType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_5.value = (ABSTRACTPAGE_NAVIGATION.services.applic.uniface.ABSTRACTPAGE_NAVIGATIONType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_5.value = (ABSTRACTPAGE_NAVIGATION.services.applic.uniface.ABSTRACTPAGE_NAVIGATIONType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5")), ABSTRACTPAGE_NAVIGATION.services.applic.uniface.ABSTRACTPAGE_NAVIGATIONType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_6.value = (ABSTRACTPAGE_NAVIGATION.services.applic.uniface.ABSTRACTPAGE_NAVIGATIONType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_6"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_6.value = (ABSTRACTPAGE_NAVIGATION.services.applic.uniface.ABSTRACTPAGE_NAVIGATIONType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_6")), ABSTRACTPAGE_NAVIGATION.services.applic.uniface.ABSTRACTPAGE_NAVIGATIONType[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void GETPORTLETCONTENT(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, java.lang.String PPORTLETID, java.lang.String PUSERID, java.lang.String POPTIONS, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2, javax.xml.rpc.holders.StringHolder PCONTENT) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[28]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "GETPORTLETCONTENT"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, PPORTLETID, PUSERID, POPTIONS});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
            try {
                PCONTENT.value = (java.lang.String) _output.get(new javax.xml.namespace.QName("", "PCONTENT"));
            } catch (java.lang.Exception _exception) {
                PCONTENT.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "PCONTENT")), java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void GETUSERIDFROMTOKEN(java.lang.String PTOKEN, javax.xml.rpc.holders.IntHolder _return, javax.xml.rpc.holders.StringHolder PUSERID, javax.xml.rpc.holders.StringHolder PTIMESTAMP) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[29]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "GETUSERIDFROMTOKEN"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {PTOKEN});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                PUSERID.value = (java.lang.String) _output.get(new javax.xml.namespace.QName("", "PUSERID"));
            } catch (java.lang.Exception _exception) {
                PUSERID.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "PUSERID")), java.lang.String.class);
            }
            try {
                PTIMESTAMP.value = (java.lang.String) _output.get(new javax.xml.namespace.QName("", "PTIMESTAMP"));
            } catch (java.lang.Exception _exception) {
                PTIMESTAMP.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "PTIMESTAMP")), java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void INSERTDOMAIN(ABSTRACTPORTAL_DOMAIN.services.applic.uniface.ABSTRACTPORTAL_DOMAINType[] GENERATED_PARAMETER_NAME_1, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_2, javax.xml.rpc.holders.IntHolder _return, ABSTRACTPORTAL_DOMAIN.services.applic.uniface.holders.ABSTRACTPORTAL_DOMAINArrayHolder GENERATED_PARAMETER_NAME_3, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[30]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "INSERTDOMAIN"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_2});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_3.value = (ABSTRACTPORTAL_DOMAIN.services.applic.uniface.ABSTRACTPORTAL_DOMAINType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_3.value = (ABSTRACTPORTAL_DOMAIN.services.applic.uniface.ABSTRACTPORTAL_DOMAINType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3")), ABSTRACTPORTAL_DOMAIN.services.applic.uniface.ABSTRACTPORTAL_DOMAINType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void INSERTDOMAINMEMBERSHIP(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[] GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[31]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "INSERTDOMAINMEMBERSHIP"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void INSERTGROUP(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, ABSTRACTGROUP.services.applic.uniface.ABSTRACTGROUPType[] GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2, ABSTRACTGROUP.services.applic.uniface.holders.ABSTRACTGROUPArrayHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[32]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "INSERTGROUP"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTGROUP.services.applic.uniface.ABSTRACTGROUPType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTGROUP.services.applic.uniface.ABSTRACTGROUPType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), ABSTRACTGROUP.services.applic.uniface.ABSTRACTGROUPType[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void INSERTGROUPPACKAGE(ABSTRACTGROUP_PACKAGE.services.applic.uniface.ABSTRACTGROUP_PACKAGEType[] GENERATED_PARAMETER_NAME_1, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_2, javax.xml.rpc.holders.IntHolder _return, ABSTRACTGROUP_PACKAGE.services.applic.uniface.holders.ABSTRACTGROUP_PACKAGEArrayHolder GENERATED_PARAMETER_NAME_3, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[33]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "INSERTGROUPPACKAGE"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_2});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_3.value = (ABSTRACTGROUP_PACKAGE.services.applic.uniface.ABSTRACTGROUP_PACKAGEType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_3.value = (ABSTRACTGROUP_PACKAGE.services.applic.uniface.ABSTRACTGROUP_PACKAGEType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3")), ABSTRACTGROUP_PACKAGE.services.applic.uniface.ABSTRACTGROUP_PACKAGEType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void INSERTPACKAGEMEMBERSHIP(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[] GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[34]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "INSERTPACKAGEMEMBERSHIP"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void INSERTPAGEMEMBERSHIP(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[] GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[35]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "INSERTPAGEMEMBERSHIP"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void INSERTPORTALPAGE(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, ABSTRACTPORTAL_PAGE.services.applic.uniface.ABSTRACTPORTAL_PAGEType[] GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2, ABSTRACTPORTAL_PAGE.services.applic.uniface.holders.ABSTRACTPORTAL_PAGEArrayHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[36]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "INSERTPORTALPAGE"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTPORTAL_PAGE.services.applic.uniface.ABSTRACTPORTAL_PAGEType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTPORTAL_PAGE.services.applic.uniface.ABSTRACTPORTAL_PAGEType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), ABSTRACTPORTAL_PAGE.services.applic.uniface.ABSTRACTPORTAL_PAGEType[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void INSERTPORTALPAGEPORTLET(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, ABSTRACTPORTAL_PAGE_PORTLET.services.applic.uniface.ABSTRACTPORTAL_PAGE_PORTLETType[] GENERATED_PARAMETER_NAME_3, java.lang.String PPAGETYPE, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[37]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "INSERTPORTALPAGEPORTLET"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3, PPAGETYPE});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void INSERTPORTLETCATEGORY(ABSTRACTPORTLET_CATEGORY.services.applic.uniface.ABSTRACTPORTLET_CATEGORYType[] GENERATED_PARAMETER_NAME_1, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_2, javax.xml.rpc.holders.IntHolder _return, ABSTRACTPORTLET_CATEGORY.services.applic.uniface.holders.ABSTRACTPORTLET_CATEGORYArrayHolder GENERATED_PARAMETER_NAME_3, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[38]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "INSERTPORTLETCATEGORY"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_2});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_3.value = (ABSTRACTPORTLET_CATEGORY.services.applic.uniface.ABSTRACTPORTLET_CATEGORYType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_3.value = (ABSTRACTPORTLET_CATEGORY.services.applic.uniface.ABSTRACTPORTLET_CATEGORYType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3")), ABSTRACTPORTLET_CATEGORY.services.applic.uniface.ABSTRACTPORTLET_CATEGORYType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void INSERTPORTLETDOMAINMEMBERSHIP(ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[] GENERATED_PARAMETER_NAME_1, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_2, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_3) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[39]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "INSERTPORTLETDOMAINMEMBERSHIP"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_2});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_3.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_3.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void INSERTPORTLETMEMBERSHIP(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[] GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[40]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "INSERTPORTLETMEMBERSHIP"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void INSERTUSERMEMBERSHIP(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, ABSTRACTPORTAL_USER_MEMBERSHIP.services.applic.uniface.ABSTRACTPORTAL_USER_MEMBERSHIPType[] GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[41]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "INSERTUSERMEMBERSHIP"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void SELECTDOMAIN(ABSTRACTPORTAL_DOMAIN.services.applic.uniface.ABSTRACTPORTAL_DOMAINType GENERATED_PARAMETER_NAME_1, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, ABSTRACTPORTAL_DOMAIN.services.applic.uniface.holders.ABSTRACTPORTAL_DOMAINArrayHolder GENERATED_PARAMETER_NAME_2, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[42]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "SELECTDOMAIN"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTPORTAL_DOMAIN.services.applic.uniface.ABSTRACTPORTAL_DOMAINType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTPORTAL_DOMAIN.services.applic.uniface.ABSTRACTPORTAL_DOMAINType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), ABSTRACTPORTAL_DOMAIN.services.applic.uniface.ABSTRACTPORTAL_DOMAINType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void SELECTDOMAINMEMBERSHIP(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2, ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.holders.ABSTRACTGROUP_MEMBERSHIPArrayHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[43]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "SELECTDOMAINMEMBERSHIP"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void SELECTFILTERDOMAIN(ABSTRACTPORTAL_DOMAIN.services.applic.uniface.ABSTRACTPORTAL_DOMAINType GENERATED_PARAMETER_NAME_1, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, ABSTRACTPORTAL_DOMAIN.services.applic.uniface.holders.ABSTRACTPORTAL_DOMAINArrayHolder GENERATED_PARAMETER_NAME_2, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[44]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "SELECTFILTERDOMAIN"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTPORTAL_DOMAIN.services.applic.uniface.ABSTRACTPORTAL_DOMAINType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTPORTAL_DOMAIN.services.applic.uniface.ABSTRACTPORTAL_DOMAINType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), ABSTRACTPORTAL_DOMAIN.services.applic.uniface.ABSTRACTPORTAL_DOMAINType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void SELECTFILTERGROUP(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, ABSTRACTGROUP.services.applic.uniface.ABSTRACTGROUPType GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2, ABSTRACTGROUP.services.applic.uniface.holders.ABSTRACTGROUPArrayHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[45]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "SELECTFILTERGROUP"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTGROUP.services.applic.uniface.ABSTRACTGROUPType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTGROUP.services.applic.uniface.ABSTRACTGROUPType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), ABSTRACTGROUP.services.applic.uniface.ABSTRACTGROUPType[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void SELECTFILTERGROUPPACKAGE(ABSTRACTGROUP_PACKAGE.services.applic.uniface.ABSTRACTGROUP_PACKAGEType GENERATED_PARAMETER_NAME_1, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, ABSTRACTGROUP_PACKAGE.services.applic.uniface.holders.ABSTRACTGROUP_PACKAGEArrayHolder GENERATED_PARAMETER_NAME_2, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[46]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "SELECTFILTERGROUPPACKAGE"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTGROUP_PACKAGE.services.applic.uniface.ABSTRACTGROUP_PACKAGEType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTGROUP_PACKAGE.services.applic.uniface.ABSTRACTGROUP_PACKAGEType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), ABSTRACTGROUP_PACKAGE.services.applic.uniface.ABSTRACTGROUP_PACKAGEType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void SELECTFILTERPACKAGEMEMBERSHIP(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2, ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.holders.ABSTRACTGROUP_MEMBERSHIPArrayHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[47]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "SELECTFILTERPACKAGEMEMBERSHIP"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void SELECTFILTERPORTALPAGE(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, ABSTRACTPORTAL_PAGE.services.applic.uniface.ABSTRACTPORTAL_PAGEType GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2, ABSTRACTPORTAL_PAGE.services.applic.uniface.holders.ABSTRACTPORTAL_PAGEArrayHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[48]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "SELECTFILTERPORTALPAGE"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTPORTAL_PAGE.services.applic.uniface.ABSTRACTPORTAL_PAGEType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTPORTAL_PAGE.services.applic.uniface.ABSTRACTPORTAL_PAGEType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), ABSTRACTPORTAL_PAGE.services.applic.uniface.ABSTRACTPORTAL_PAGEType[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void SELECTFILTERPORTALPORTLET(ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType GENERATED_PARAMETER_NAME_1, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, ABSTRACTPORTLET.services.applic.uniface.holders.ABSTRACTPORTLETArrayHolder GENERATED_PARAMETER_NAME_2, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[49]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "SELECTFILTERPORTALPORTLET"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void SELECTFILTERPORTLET(ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType GENERATED_PARAMETER_NAME_1, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, ABSTRACTPORTLET.services.applic.uniface.holders.ABSTRACTPORTLETArrayHolder GENERATED_PARAMETER_NAME_2, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[50]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "SELECTFILTERPORTLET"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void SELECTFILTERPORTLETBYUSER(ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType GENERATED_PARAMETER_NAME_2, java.lang.String PUSERID, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_4, javax.xml.rpc.holders.IntHolder _return, ABSTRACTPORTLET.services.applic.uniface.holders.ABSTRACTPORTLETArrayHolder GENERATED_PARAMETER_NAME_3, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_5) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[51]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "SELECTFILTERPORTLETBYUSER"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_2, PUSERID, GENERATED_PARAMETER_NAME_4});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_3.value = (ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_3.value = (ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3")), ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_5.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_5.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void SELECTFILTERPORTLETCATEGORY(ABSTRACTPORTLET_CATEGORY.services.applic.uniface.ABSTRACTPORTLET_CATEGORYType GENERATED_PARAMETER_NAME_1, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, ABSTRACTPORTLET_CATEGORY.services.applic.uniface.holders.ABSTRACTPORTLET_CATEGORYArrayHolder GENERATED_PARAMETER_NAME_2, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[52]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "SELECTFILTERPORTLETCATEGORY"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTPORTLET_CATEGORY.services.applic.uniface.ABSTRACTPORTLET_CATEGORYType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTPORTLET_CATEGORY.services.applic.uniface.ABSTRACTPORTLET_CATEGORYType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), ABSTRACTPORTLET_CATEGORY.services.applic.uniface.ABSTRACTPORTLET_CATEGORYType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void SELECTFILTERUSER(ABSTRACTPORTAL_USER.services.applic.uniface.ABSTRACTPORTAL_USERType GENERATED_PARAMETER_NAME_1, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, ABSTRACTPORTAL_USER.services.applic.uniface.holders.ABSTRACTPORTAL_USERArrayHolder GENERATED_PARAMETER_NAME_2, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[53]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "SELECTFILTERUSER"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTPORTAL_USER.services.applic.uniface.ABSTRACTPORTAL_USERType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTPORTAL_USER.services.applic.uniface.ABSTRACTPORTAL_USERType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), ABSTRACTPORTAL_USER.services.applic.uniface.ABSTRACTPORTAL_USERType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void SELECTGROUP(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, ABSTRACTGROUP.services.applic.uniface.ABSTRACTGROUPType GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2, ABSTRACTGROUP.services.applic.uniface.holders.ABSTRACTGROUPArrayHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[54]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "SELECTGROUP"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTGROUP.services.applic.uniface.ABSTRACTGROUPType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTGROUP.services.applic.uniface.ABSTRACTGROUPType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), ABSTRACTGROUP.services.applic.uniface.ABSTRACTGROUPType[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void SELECTGROUPPACKAGE(ABSTRACTGROUP_PACKAGE.services.applic.uniface.ABSTRACTGROUP_PACKAGEType GENERATED_PARAMETER_NAME_1, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, ABSTRACTGROUP_PACKAGE.services.applic.uniface.holders.ABSTRACTGROUP_PACKAGEArrayHolder GENERATED_PARAMETER_NAME_2, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[55]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "SELECTGROUPPACKAGE"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTGROUP_PACKAGE.services.applic.uniface.ABSTRACTGROUP_PACKAGEType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTGROUP_PACKAGE.services.applic.uniface.ABSTRACTGROUP_PACKAGEType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), ABSTRACTGROUP_PACKAGE.services.applic.uniface.ABSTRACTGROUP_PACKAGEType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void SELECTHTMLPORTLETCONTENT(java.lang.String PUSERID, java.lang.String PPORTLETID, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, ABSTRACTHTML_PORTLET_PROPERTY.services.applic.uniface.holders.ABSTRACTHTML_PORTLET_PROPERTYTypeHolder GENERATED_PARAMETER_NAME_4, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_5) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[56]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "SELECTHTMLPORTLETCONTENT"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {PUSERID, PPORTLETID, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTHTML_PORTLET_PROPERTY.services.applic.uniface.ABSTRACTHTML_PORTLET_PROPERTYType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTHTML_PORTLET_PROPERTY.services.applic.uniface.ABSTRACTHTML_PORTLET_PROPERTYType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), ABSTRACTHTML_PORTLET_PROPERTY.services.applic.uniface.ABSTRACTHTML_PORTLET_PROPERTYType.class);
            }
            try {
                GENERATED_PARAMETER_NAME_5.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_5.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void SELECTJOBDETAIL(ABSTRACTJOB_DETAIL.services.applic.uniface.ABSTRACTJOB_DETAILType GENERATED_PARAMETER_NAME_1, javax.xml.rpc.holders.IntHolder _return, ABSTRACTJOB_DETAIL.services.applic.uniface.holders.ABSTRACTJOB_DETAILArrayHolder GENERATED_PARAMETER_NAME_2, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_3) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[57]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "SELECTJOBDETAIL"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTJOB_DETAIL.services.applic.uniface.ABSTRACTJOB_DETAILType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTJOB_DETAIL.services.applic.uniface.ABSTRACTJOB_DETAILType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), ABSTRACTJOB_DETAIL.services.applic.uniface.ABSTRACTJOB_DETAILType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_3.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_3.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void SELECTJOBTRIGGER(ABSTRACTJOB_TRIGGER.services.applic.uniface.ABSTRACTJOB_TRIGGERType GENERATED_PARAMETER_NAME_1, javax.xml.rpc.holders.IntHolder _return, ABSTRACTJOB_TRIGGER.services.applic.uniface.holders.ABSTRACTJOB_TRIGGERArrayHolder GENERATED_PARAMETER_NAME_2, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_3) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[58]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "SELECTJOBTRIGGER"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTJOB_TRIGGER.services.applic.uniface.ABSTRACTJOB_TRIGGERType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTJOB_TRIGGER.services.applic.uniface.ABSTRACTJOB_TRIGGERType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), ABSTRACTJOB_TRIGGER.services.applic.uniface.ABSTRACTJOB_TRIGGERType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_3.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_3.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void SELECTPACKAGEMEMBERSHIP(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2, ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.holders.ABSTRACTGROUP_MEMBERSHIPArrayHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[59]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "SELECTPACKAGEMEMBERSHIP"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void SELECTPAGEMEMBERSHIP(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2, ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.holders.ABSTRACTGROUP_MEMBERSHIPArrayHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[60]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "SELECTPAGEMEMBERSHIP"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void SELECTPORTALPAGE(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, ABSTRACTPORTAL_PAGE.services.applic.uniface.ABSTRACTPORTAL_PAGEType GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2, ABSTRACTPORTAL_PAGE.services.applic.uniface.holders.ABSTRACTPORTAL_PAGEArrayHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[61]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "SELECTPORTALPAGE"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTPORTAL_PAGE.services.applic.uniface.ABSTRACTPORTAL_PAGEType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTPORTAL_PAGE.services.applic.uniface.ABSTRACTPORTAL_PAGEType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), ABSTRACTPORTAL_PAGE.services.applic.uniface.ABSTRACTPORTAL_PAGEType[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void SELECTPORTALPAGEPORTLET(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, ABSTRACTPORTAL_PAGE_PORTLET.services.applic.uniface.ABSTRACTPORTAL_PAGE_PORTLETType GENERATED_PARAMETER_NAME_3, java.lang.String PPAGETYPE, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2, ABSTRACTPORTAL_PAGE_PORTLET.services.applic.uniface.holders.ABSTRACTPORTAL_PAGE_PORTLETArrayHolder GENERATED_PARAMETER_NAME_5) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[62]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "SELECTPORTALPAGEPORTLET"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3, PPAGETYPE});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
            try {
                GENERATED_PARAMETER_NAME_5.value = (ABSTRACTPORTAL_PAGE_PORTLET.services.applic.uniface.ABSTRACTPORTAL_PAGE_PORTLETType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_5.value = (ABSTRACTPORTAL_PAGE_PORTLET.services.applic.uniface.ABSTRACTPORTAL_PAGE_PORTLETType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5")), ABSTRACTPORTAL_PAGE_PORTLET.services.applic.uniface.ABSTRACTPORTAL_PAGE_PORTLETType[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void SELECTPORTALPORTLET(ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType GENERATED_PARAMETER_NAME_1, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, ABSTRACTPORTLET.services.applic.uniface.holders.ABSTRACTPORTLETArrayHolder GENERATED_PARAMETER_NAME_2, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[63]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "SELECTPORTALPORTLET"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void SELECTPORTLET(ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType GENERATED_PARAMETER_NAME_1, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, ABSTRACTPORTLET.services.applic.uniface.holders.ABSTRACTPORTLETArrayHolder GENERATED_PARAMETER_NAME_2, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[64]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "SELECTPORTLET"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void SELECTPORTLETCATEGORY(ABSTRACTPORTLET_CATEGORY.services.applic.uniface.ABSTRACTPORTLET_CATEGORYType GENERATED_PARAMETER_NAME_1, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, ABSTRACTPORTLET_CATEGORY.services.applic.uniface.holders.ABSTRACTPORTLET_CATEGORYArrayHolder GENERATED_PARAMETER_NAME_2, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[65]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "SELECTPORTLETCATEGORY"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTPORTLET_CATEGORY.services.applic.uniface.ABSTRACTPORTLET_CATEGORYType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTPORTLET_CATEGORY.services.applic.uniface.ABSTRACTPORTLET_CATEGORYType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), ABSTRACTPORTLET_CATEGORY.services.applic.uniface.ABSTRACTPORTLET_CATEGORYType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void SELECTPORTLETDOMAINMEMBERSHIP(ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType GENERATED_PARAMETER_NAME_1, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.holders.ABSTRACTGROUP_MEMBERSHIPArrayHolder GENERATED_PARAMETER_NAME_2, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[66]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "SELECTPORTLETDOMAINMEMBERSHIP"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void SELECTPORTLETMEMBERSHIP(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2, ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.holders.ABSTRACTGROUP_MEMBERSHIPArrayHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[67]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "SELECTPORTLETMEMBERSHIP"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), ABSTRACTGROUP_MEMBERSHIP.services.applic.uniface.ABSTRACTGROUP_MEMBERSHIPType[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void SELECTTEXTPORTLETCONTENT(java.lang.String PUSERID, java.lang.String PPORTLETID, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, ABSTRACTHTML_PORTLET_PROPERTY.services.applic.uniface.holders.ABSTRACTHTML_PORTLET_PROPERTYTypeHolder GENERATED_PARAMETER_NAME_4, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_5) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[68]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "SELECTTEXTPORTLETCONTENT"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {PUSERID, PPORTLETID, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTHTML_PORTLET_PROPERTY.services.applic.uniface.ABSTRACTHTML_PORTLET_PROPERTYType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTHTML_PORTLET_PROPERTY.services.applic.uniface.ABSTRACTHTML_PORTLET_PROPERTYType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), ABSTRACTHTML_PORTLET_PROPERTY.services.applic.uniface.ABSTRACTHTML_PORTLET_PROPERTYType.class);
            }
            try {
                GENERATED_PARAMETER_NAME_5.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_5.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void SELECTUSER(ABSTRACTPORTAL_USER.services.applic.uniface.ABSTRACTPORTAL_USERType GENERATED_PARAMETER_NAME_1, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, ABSTRACTPORTAL_USER.services.applic.uniface.holders.ABSTRACTPORTAL_USERArrayHolder GENERATED_PARAMETER_NAME_2, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[69]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "SELECTUSER"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTPORTAL_USER.services.applic.uniface.ABSTRACTPORTAL_USERType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (ABSTRACTPORTAL_USER.services.applic.uniface.ABSTRACTPORTAL_USERType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), ABSTRACTPORTAL_USER.services.applic.uniface.ABSTRACTPORTAL_USERType[].class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void SELECTUSERMEMBERSHIP(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, ABSTRACTPORTAL_USER_MEMBERSHIP.services.applic.uniface.ABSTRACTPORTAL_USER_MEMBERSHIPType GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2, ABSTRACTPORTAL_USER_MEMBERSHIP.services.applic.uniface.holders.ABSTRACTPORTAL_USER_MEMBERSHIPArrayHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[70]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "SELECTUSERMEMBERSHIP"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTPORTAL_USER_MEMBERSHIP.services.applic.uniface.ABSTRACTPORTAL_USER_MEMBERSHIPType[]) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (ABSTRACTPORTAL_USER_MEMBERSHIP.services.applic.uniface.ABSTRACTPORTAL_USER_MEMBERSHIPType[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), ABSTRACTPORTAL_USER_MEMBERSHIP.services.applic.uniface.ABSTRACTPORTAL_USER_MEMBERSHIPType[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void UPDATEDOMAIN(ABSTRACTPORTAL_DOMAIN.services.applic.uniface.ABSTRACTPORTAL_DOMAINType[] GENERATED_PARAMETER_NAME_1, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_2, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_3) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[71]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "UPDATEDOMAIN"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_2});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_3.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_3.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void UPDATEGROUP(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, ABSTRACTGROUP.services.applic.uniface.ABSTRACTGROUPType[] GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[72]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "UPDATEGROUP"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void UPDATEGROUPPACKAGE(ABSTRACTGROUP_PACKAGE.services.applic.uniface.ABSTRACTGROUP_PACKAGEType[] GENERATED_PARAMETER_NAME_1, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_2, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_3) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[73]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "UPDATEGROUPPACKAGE"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_2});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_3.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_3.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void UPDATEHTMLPORTLETCONTENT(java.lang.String PUSERID, java.lang.String PPORTLETID, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_3, ABSTRACTHTML_PORTLET_PROPERTY.services.applic.uniface.ABSTRACTHTML_PORTLET_PROPERTYType GENERATED_PARAMETER_NAME_4, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_5) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[74]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "UPDATEHTMLPORTLETCONTENT"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {PUSERID, PPORTLETID, GENERATED_PARAMETER_NAME_3, GENERATED_PARAMETER_NAME_4});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_5.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_5.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void UPDATEPORTALPAGE(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, ABSTRACTPORTAL_PAGE.services.applic.uniface.ABSTRACTPORTAL_PAGEType[] GENERATED_PARAMETER_NAME_3, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[75]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "UPDATEPORTALPAGE"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void UPDATEPORTLET(ABSTRACTPORTLET.services.applic.uniface.ABSTRACTPORTLETType[] GENERATED_PARAMETER_NAME_1, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_2, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_3) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[76]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "UPDATEPORTLET"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_2});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_3.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_3.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void UPDATEPORTLETCATEGORY(ABSTRACTPORTLET_CATEGORY.services.applic.uniface.ABSTRACTPORTLET_CATEGORYType[] GENERATED_PARAMETER_NAME_1, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_2, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_3) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[77]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "UPDATEPORTLETCATEGORY"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_2});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_3.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_3.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void UPDATETEXTPORTLETCONTENT(java.lang.String PUSERID, java.lang.String PPORTLETID, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_3, ABSTRACTHTML_PORTLET_PROPERTY.services.applic.uniface.ABSTRACTHTML_PORTLET_PROPERTYType GENERATED_PARAMETER_NAME_4, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_5) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[78]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "UPDATETEXTPORTLETCONTENT"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {PUSERID, PPORTLETID, GENERATED_PARAMETER_NAME_3, GENERATED_PARAMETER_NAME_4});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_5.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_5.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void UPDATEUSER(ABSTRACTPORTAL_USER.services.applic.uniface.ABSTRACTPORTAL_USERType[] GENERATED_PARAMETER_NAME_1, STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_2, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_3) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[79]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "UPDATEUSER"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_2});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_3.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_3.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_3")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void UPDATEUSERPASSWORD(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, java.lang.String PUSERID, java.lang.String PPASSWORD, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_4) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[80]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "UPDATEUSERPASSWORD"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, PUSERID, PPASSWORD});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void VALIDATEPORTALUSER(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, STANDARDCOOKIE_CONTEXT.services.applic.uniface.STANDARDCOOKIE_CONTEXTType GENERATED_PARAMETER_NAME_2, javax.xml.rpc.holders.IntHolder _return, javax.xml.rpc.holders.StringHolder PUSERID, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_4, STANDARDCOOKIE_CONTEXT.services.applic.uniface.holders.STANDARDCOOKIE_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_5, javax.xml.rpc.holders.BooleanHolder PISVALIDATED) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[81]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "VALIDATEPORTALUSER"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1, GENERATED_PARAMETER_NAME_2});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                PUSERID.value = (java.lang.String) _output.get(new javax.xml.namespace.QName("", "PUSERID"));
            } catch (java.lang.Exception _exception) {
                PUSERID.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "PUSERID")), java.lang.String.class);
            }
            try {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_4.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_4")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
            try {
                GENERATED_PARAMETER_NAME_5.value = (STANDARDCOOKIE_CONTEXT.services.applic.uniface.STANDARDCOOKIE_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_5.value = (STANDARDCOOKIE_CONTEXT.services.applic.uniface.STANDARDCOOKIE_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_5")), STANDARDCOOKIE_CONTEXT.services.applic.uniface.STANDARDCOOKIE_CONTEXTType.class);
            }
            try {
                PISVALIDATED.value = ((java.lang.Boolean) _output.get(new javax.xml.namespace.QName("", "PISVALIDATED"))).booleanValue();
            } catch (java.lang.Exception _exception) {
                PISVALIDATED.value = ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "PISVALIDATED")), boolean.class)).booleanValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void VALIDATEUSER(STANDARDSECURITY_CONTEXT.services.applic.uniface.STANDARDSECURITY_CONTEXTType GENERATED_PARAMETER_NAME_1, javax.xml.rpc.holders.IntHolder _return, STANDARDSTATUS_CONTEXT.services.applic.uniface.holders.STANDARDSTATUS_CONTEXTTypeHolder GENERATED_PARAMETER_NAME_2, javax.xml.rpc.holders.BooleanHolder PISVALIDATED) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[82]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "VALIDATEUSER"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {GENERATED_PARAMETER_NAME_1});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                _return.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "return"))).intValue();
            } catch (java.lang.Exception _exception) {
                _return.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "return")), int.class)).intValue();
            }
            try {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) _output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2"));
            } catch (java.lang.Exception _exception) {
                GENERATED_PARAMETER_NAME_2.value = (STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "GENERATED_PARAMETER_NAME_2")), STANDARDSTATUS_CONTEXT.services.applic.uniface.STANDARDSTATUS_CONTEXTType.class);
            }
            try {
                PISVALIDATED.value = ((java.lang.Boolean) _output.get(new javax.xml.namespace.QName("", "PISVALIDATED"))).booleanValue();
            } catch (java.lang.Exception _exception) {
                PISVALIDATED.value = ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "PISVALIDATED")), boolean.class)).booleanValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
