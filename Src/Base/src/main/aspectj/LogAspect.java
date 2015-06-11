import org.apache.log4j.*;
import org.aspectj.lang.*;
import com.compuware.toolbox.util.logging.LoggerManager;

public aspect LogAspect {

    Logger _logger = LoggerManager.getLogger("Ihm");

    LogAspect() {
    }

    pointcut traceMethods()
        : call(public * com.compuware.caqs.presentation.admin.actions.ElementUpdateAction.*(..)) && !within(LogAspect);

    before() : traceMethods() {

        //if (_logger.isEnabledFor(Level.DEBUG)) {

            Signature sig = thisJoinPointStaticPart.getSignature();

            _logger.debug(
                        "Entering method ["

                        + sig.getDeclaringType().getName() + "."

                        + sig.getName() + "]");

        //}

    }

}
