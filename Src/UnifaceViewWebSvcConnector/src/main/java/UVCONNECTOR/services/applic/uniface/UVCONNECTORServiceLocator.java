/**
 * UVCONNECTORServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package UVCONNECTOR.services.applic.uniface;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.holders.IntHolder;
import javax.xml.rpc.holders.StringHolder;

public class UVCONNECTORServiceLocator extends org.apache.axis.client.Service implements UVCONNECTOR.services.applic.uniface.UVCONNECTORService {

    public UVCONNECTORServiceLocator() {
    }


    public UVCONNECTORServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public UVCONNECTORServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for UVCONNECTOR
    private java.lang.String UVCONNECTOR_address = "http://localhost:8370/unifaceview/services/uvconnector";

    public java.lang.String getUVCONNECTORAddress() {
        return UVCONNECTOR_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String UVCONNECTORWSDDServiceName = "UVCONNECTOR";

    public java.lang.String getUVCONNECTORWSDDServiceName() {
        return UVCONNECTORWSDDServiceName;
    }

    public void setUVCONNECTORWSDDServiceName(java.lang.String name) {
        UVCONNECTORWSDDServiceName = name;
    }

    public UVCONNECTOR.services.applic.uniface.UVCONNECTORPortType getUVCONNECTOR() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(UVCONNECTOR_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getUVCONNECTOR(endpoint);
    }

    public UVCONNECTOR.services.applic.uniface.UVCONNECTORPortType getUVCONNECTOR(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            UVCONNECTOR.services.applic.uniface.UVCONNECTORBindingStub _stub = new UVCONNECTOR.services.applic.uniface.UVCONNECTORBindingStub(portAddress, this);
            _stub.setPortName(getUVCONNECTORWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setUVCONNECTOREndpointAddress(java.lang.String address) {
        UVCONNECTOR_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (UVCONNECTOR.services.applic.uniface.UVCONNECTORPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                UVCONNECTOR.services.applic.uniface.UVCONNECTORBindingStub _stub = new UVCONNECTOR.services.applic.uniface.UVCONNECTORBindingStub(new java.net.URL(UVCONNECTOR_address), this);
                _stub.setPortName(getUVCONNECTORWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("UVCONNECTOR".equals(inputPortName)) {
            return getUVCONNECTOR();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "UVCONNECTORService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:uniface:applic:services:UVCONNECTOR", "UVCONNECTOR"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("UVCONNECTOR".equals(portName)) {
            setUVCONNECTOREndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

    public static void main(String[] args) throws ServiceException, RemoteException {
    	UVCONNECTORServiceLocator locator = new UVCONNECTORServiceLocator();
    	UVCONNECTORPortType connector = locator.getUVCONNECTOR();
    	IntHolder _return = new IntHolder();
    	StringHolder PUSERID = new StringHolder();
    	StringHolder PTIMESTAMP = new StringHolder();
    	connector.GETUSERIDFROMTOKEN("c+Byl7pqY24+3yrXX+mzhEmrjBiMq/au", _return, PUSERID, PTIMESTAMP);
    	System.out.println(PUSERID.value);
    }
    
    
    
}
