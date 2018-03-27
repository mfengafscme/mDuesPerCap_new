package org.afscme.enterprise.minimumdues.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.upload.FormFile;

import org.afscme.enterprise.common.web.SearchForm;

/**
 * @struts:form name="fileUploadActionForm"
 */
public class FileUploadActionForm extends ActionForm {
    private String enterDataButton;
    private FormFile file;
    private String message;

    public FileUploadActionForm() {
        super();
    // TODO Auto-generated constructor stub
    }

    /**
     * @return the file
     */
    public FormFile getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(FormFile file) {
        this.file = file;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

	public ActionErrors validate(ActionMapping mapping,
	   HttpServletRequest request) {

	    ActionErrors errors = new ActionErrors();

	    if( getFile().getFileSize()== 0){
	       errors.add("common.file.err",
	    	new ActionMessage("error.common.file.required"));
	       return errors;
	    }

	    //only allow textfile to upload
	    /*
	    if(!"text/plain".equals(getFile().getContentType())){
	        errors.add("common.file.err.ext",
	    	 new ActionMessage("error.common.file.textfile.only"));
	        return errors;
	    } */
 		if (!getFile().getContentType().equals("application/vnd.ms-excel")) {
    		errors.add("file", new ActionMessage("error.common.file.excel.only"));
    	}

            //file size cant larger than 10kb
	    System.out.println(getFile().getFileSize());
	    /*
	    if(getFile().getFileSize() > 10240) { //10kb
	       errors.add("common.file.err.size", new ActionMessage("error.common.file.size.limit"));
	       return errors;
	    }
		*/

	    return errors;
	}

  /** Getter for property enterDataButton.
   * @return Value of property enterDataButton.
   *
   */
  public java.lang.String getEnterDataButton() {
      return enterDataButton;
  }

  /** Setter for property enterDataButton.
   * @param enterDataButton New value of property enterDataButton.
   *
   */
  public void setEnterDataButton(java.lang.String enterDataButton) {
      this.enterDataButton = enterDataButton;
  }


}
