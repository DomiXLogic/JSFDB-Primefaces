
import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@RequestScoped
public class GrowlView {

    public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public void showInfo() {
        addMessage(FacesMessage.SEVERITY_INFO, "Info Message", "Message Content");
    }

    public void showWarn() {
        addMessage(FacesMessage.SEVERITY_WARN, "Warn Message", "Message Content");
    }

    public void showError() {
        addMessage(FacesMessage.SEVERITY_ERROR, "Error Message", "Message Content");
    }

    public void showSticky() {
        FacesContext.getCurrentInstance().addMessage("sticky-key", new FacesMessage(FacesMessage.SEVERITY_INFO, "Sticky Message", "Message Content"));
    }

    
}