package com.compuware.caqs.workflow.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ow2.bonita.definition.TxHook;
import org.ow2.bonita.facade.APIAccessor;
import org.ow2.bonita.facade.runtime.ActivityInstance;
import org.ow2.bonita.util.BonitaException;

/**
 * Print all variable values.
 * @author cwfr-fdubois
 *
 */
public class HttpCallHook implements TxHook {

	// declared public as they are referenced from FailedMessageHook
    public static final String HTTP_SERVER_CONTEXT_PATH = "HTTP_SERVER_CONTEXT_PATH";
    public static final String HTTP_SERVER_CONTEXT_PATH_2 = "HTTP_SERVER_CONTEXT_PATH_2";
    public static final String HTTP_SERVER_CONTEXT_PATH_3 = "HTTP_SERVER_CONTEXT_PATH_3";
    
    private static final String URL = "URL";
    private String url = null;
    private static final Pattern SUCCESS_PATTERN = Pattern.compile("<success>\\s*true\\s*</success>");
    private static final Pattern MESSAGE_PATTERN = Pattern.compile("<message>(.*)</message>");
    
    protected boolean useAlternateServerContext = false;
    protected boolean useSecondAlternateServerContext = false;
    protected boolean useOnlySecondAlternateServerContext = false;
    
    /**
     *
     */
    public void execute(APIAccessor accessor, ActivityInstance activity) throws Exception {
        this.execute(accessor, activity, true);
    }

    /**
     *
     */
    public void execute(APIAccessor accessor, ActivityInstance activity, boolean updateVariableResult) throws Exception
    {
        if(useOnlySecondAlternateServerContext){
                startTask(accessor,activity,updateVariableResult, HTTP_SERVER_CONTEXT_PATH_3);           
        } else if (useAlternateServerContext) {
            boolean isSuccess=startTask(accessor,activity,updateVariableResult, HTTP_SERVER_CONTEXT_PATH_2);
                // to prevent from hiding a problem that could have happened during call to
                // first alternate server, we call second alternate server only if call
                // to first went well.
            if(isSuccess && useSecondAlternateServerContext){
                startTask(accessor,activity,updateVariableResult, HTTP_SERVER_CONTEXT_PATH_3);
            }
        } else {
            startTask(accessor,activity,updateVariableResult, HTTP_SERVER_CONTEXT_PATH);
        }

    }

    private boolean startTask(APIAccessor accessor, ActivityInstance activity, boolean updateVariableResult, String context) throws Exception
    {

        String serverBase;
        boolean isSuccess=true;

        serverBase = (String) accessor.getQueryRuntimeAPI().getProcessInstanceVariable(activity.getProcessInstanceUUID(), context);
        if (url == null) {
            url = (String) activity.getVariableValueBeforeStarted(URL);
        }

        accessor.getRuntimeAPI().setProcessInstanceVariable(activity.getProcessInstanceUUID(), "lastUrl", url);
        
        if (url != null) {
            ResultBean res = callUrl(serverBase + url.replaceAll(" ", "%20"), 4, 1000, accessor, activity);
            if (updateVariableResult) {
                accessor.getRuntimeAPI().setProcessInstanceVariable(activity.getProcessInstanceUUID(), "result", "" + res.isSuccess());
                accessor.getRuntimeAPI().setProcessInstanceVariable(activity.getProcessInstanceUUID(), "message", "" + res.getMessageList().toString());
            }
            isSuccess=res.isSuccess();
        } else {
            accessor.getRuntimeAPI().setProcessInstanceVariable(activity.getProcessInstanceUUID(), "result", "false");
            accessor.getRuntimeAPI().setProcessInstanceVariable(activity.getProcessInstanceUUID(), "message", "No url specified");
            isSuccess=false;
        }
        return isSuccess;
    }

    /**
     * Call the given url with the given number of try and delay between them in
     * case of error.
     * @param url the url to call.
     * @param nbOfTry the number of try that should be done if the connection fail.
     * @param tryDelay the delay between two try.
     * @return the result of the url call.
     */
    public ResultBean callUrl(String url, int nbOfTry, int tryDelay, APIAccessor accessor, ActivityInstance activity) {
        ResultBean result = new ResultBean();
        int tryIdx = 0;

        while (tryIdx < nbOfTry) {
            boolean errorexceptionOccured = false;
            boolean needRetry = false;
            try {
                Thread.sleep(tryIdx * tryDelay);
                String inputStream = callUrl(url);
                if (inputStream != null) {
                    result.setSuccess(isSuccess(inputStream));
                    result.addMessage(getMessage(inputStream));
                    postCallSetVariables(inputStream, accessor, activity);
                }
            } catch (IOException e) {
                errorexceptionOccured = true;
                needRetry = needRetry(e);
                result.addMessage("ERROR : " + e.getMessage());
            } catch (Exception e) {
                result.addMessage("ERROR : " + e.getMessage());
            }
            if (errorexceptionOccured && needRetry) {
                tryIdx++;
            } else {
                tryIdx = nbOfTry;
            }
        }
        return result;
    }

    /** Check the http call input stream for success message.
     * @param inputStream the given input stream.
     * @return true if the message contains <success>true</success>, false otherwise.
     */
    private boolean isSuccess(String inputStream) {
        boolean result = false;
        Matcher m = SUCCESS_PATTERN.matcher(inputStream);
        result = m != null && m.find();
        return result;
    }

    /**
     * Get the message from the http call input stream.
     * @param inputStream the given input stream.
     * @return the message contained into the http call input stream <message>...</message>.
     */
    private String getMessage(String inputStream) {
        String result = "";
        Matcher m = MESSAGE_PATTERN.matcher(inputStream);
        if (m != null && m.find()) {
            result = m.group(0);
        }
        return result;
    }

    /**
     * Called after http call to change variables values.
     * @param inputStream the result stream.
     */
    protected void postCallSetVariables(String inputStream, APIAccessor accessor, ActivityInstance activity) throws BonitaException {
    }

    /**
     * Check if the exception that occured needs a new try or not.
     * @param e the exception that occured.
     * @return true if a new try is needed, false otherwise.
     */
    private boolean needRetry(IOException e) {
        boolean result = false;
        if (e != null) {
            String msg = e.getMessage();
            if (msg != null) {
                if (msg.contains("Connection refused")) {
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * Call the given url.
     * @param url the url to call.
     * @return the response string or an error message.
     * @throws java.io.IOException
     */
    private String callUrl(String url) throws IOException {
        StringBuffer result = new StringBuffer();
        BufferedReader inputFromServlet = null;
        try {
            System.out.println("URL:" + url);
            URL servleturl = new URL(url);

            URLConnection servletConnection = servleturl.openConnection();

            // Read the input from the servlet.
            System.out.println("Getting input stream");
            inputFromServlet = new BufferedReader(new InputStreamReader(servletConnection.getInputStream()));
            String oes = "";
            while (oes != null) {
                oes = inputFromServlet.readLine();
                if (oes != null) {
                    result.append(oes);
                }
            }
        } finally {
            try {
                if (inputFromServlet != null) {
                    inputFromServlet.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(result.toString());
        return result.toString();
    }

    protected void setUrl(String url) {
        this.url = url;
    }
}
