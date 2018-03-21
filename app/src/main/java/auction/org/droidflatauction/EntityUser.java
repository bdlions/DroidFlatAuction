package auction.org.droidflatauction;


import com.bdlions.dto.response.ClientResponse;

/**
 *
 * //@author nazmul hasan
 */

public class EntityUser implements java.io.Serializable {

    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "id")
    private int id;

    //@Column(name = "user_name")
    private String userName;

    //@Column(name = "email")
    private String email;

    //@Column(name = "password")
    private String password;

    //@Column(name = "first_name")
    private String firstName;

    //@Column(name = "last_name")
    private String lastName;

    //@Column(name = "img")
    private String img;
    
    //@Column(name = "agent_logo")
    private String agentLogo;
    
    //@Column(name = "document")
    private String document;
    
    //@Column(name = "business_name")
    private String businessName;
    
    //@Column(name = "address")
    private String address;
    
    //@Column(name = "is_verified", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isVerified;
    
    //@Column(name = "cell")
    private String cell;
    
    //@Column(name = "gender_id", nullable = true, columnDefinition = "int default 0")
    private int genderId;
    
    //@Column(name = "gender_title", length = 200)
    private String genderTitle;

    //@Column(name = "account_status_id", nullable = false, columnDefinition = "int default 1")
    private int accountStatusId;
    
    //@Column(name = "email_verification_code", length = 200)
    private String emailVerificationCode;
    
    //@Column(name = "created_on", length = 11, columnDefinition = "int(11) unsigned DEFAULT 0")
    private long createdOn;

    //@Column(name = "modified_on", length = 11, columnDefinition = "int(11) unsigned DEFAULT 0")
    private long modifiedOn;

    public int getAccountStatusId() {
        return accountStatusId;
    }

    public void setAccountStatusId(int accountStatusId) {
        this.accountStatusId = accountStatusId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(long createdOn) {
        this.createdOn = createdOn;
    }

    public long getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(long modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getAgentLogo() {
        return agentLogo;
    }

    public void setAgentLogo(String agentLogo) {
        this.agentLogo = agentLogo;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isIsVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }

    public String getGenderTitle() {
        return genderTitle;
    }

    public void setGenderTitle(String genderTitle) {
        this.genderTitle = genderTitle;
    }

    public String getEmailVerificationCode() {
        return emailVerificationCode;
    }

    public void setEmailVerificationCode(String emailVerificationCode) {
        this.emailVerificationCode = emailVerificationCode;
    }
    
}
