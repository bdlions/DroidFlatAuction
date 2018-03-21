package auction.org.droidflatauction;

/**
 *
 * //@author nazmul hasan
 */
public class EntityProduct {

    private int id;
    
    //@Column(name = "reference_id", length = 100)
    private String referenceId;
    
    //@Column(name = "user_id")    
    private int userId;
    
    //@Column(name = "category_id", columnDefinition = "int(11) unsigned DEFAULT 1")
    private int categoryId;

    //@Column(name = "category_title", length = 500)
    private String categoryTitle;
    
    //@Column(name = "size_id", columnDefinition = "int(11) unsigned DEFAULT 1")
    private int sizeId;

    //@Column(name = "size_title", length = 500)
    private String sizeTitle;
    
    //@Column(name = "type_id", columnDefinition = "int(11) unsigned DEFAULT 1")
    private int typeId;

    //@Column(name = "type_title", length = 500)
    private String typeTitle;
    
    //@Column(name = "min_stay_id", columnDefinition = "int(11) unsigned DEFAULT 1")
    private int minStayId;

    //@Column(name = "min_stay_title", length = 500)
    private String minStayTitle;
    
    //@Column(name = "max_stay_id", columnDefinition = "int(11) unsigned DEFAULT 1")
    private int maxStayId;

    //@Column(name = "max_stay_title", length = 500)
    private String maxStayTitle;
    
    //@Column(name = "smoking_id", columnDefinition = "int(11) unsigned DEFAULT 1")    
    private int smokingId;

    //@Column(name = "smoking_title", length = 500)
    private String smokingTitle;
    
    //@Column(name = "occupation_id", columnDefinition = "int(11) unsigned DEFAULT 1")    
    private int occupationId;

    //@Column(name = "occupation_title", length = 500)
    private String occupationTitle;
    
    //@Column(name = "pet_id", columnDefinition = "int(11) unsigned DEFAULT 1")    
    private int petId;

    //@Column(name = "pet_title", length = 500)
    private String petTitle;
    
    //@Column(name = "location_id", columnDefinition = "int(11) unsigned DEFAULT 1")    
    private int locationId;

    //@Column(name = "location_title", length = 500)
    private String locationTitle;
    
    //@Column(name = "postcode", length = 200)
    private String postcode;
    
    //@Column(name = "lat", columnDefinition = "double DEFAULT 0")    
    private double lat;
    
    //@Column(name = "lon", columnDefinition = "double DEFAULT 0")    
    private double lon;
    
    //@Column(name = "amenity_ids", length = 500)
    private String amenityIds;
    
    //@Column(name = "amenity_titles", length = 500)
    private String amenityTitles;
    
    //@Column(name = "unix_available_from", length = 11, columnDefinition = "int(11) unsigned DEFAULT 0")
    private long unixAvailableFrom;
    //@Column(name = "available_from", length = 500)
    private String availableFrom;
    
    //@Column(name = "unix_available_to", length = 11, columnDefinition = "int(11) unsigned DEFAULT 0")
    private long unixAvailableTo;
    //@Column(name = "available_to", length = 500)
    private String availableTo;
    
    //@Column(name = "on_going", columnDefinition = "boolean DEFAULT 0")
    private boolean onGoing;
    
    //@Column(name = "availability_ids", length = 500)
    private String availabilityIds;
    
    //@Column(name = "availability_titles", length = 500)
    private String availabilityTitles;
    
    //@Column(name = "img", length = 1000)
    private String img;  
    
    //@Column(name = "images", length = 1000)
    private String images;    
    
    //@Column(name = "auction_start_date", length = 500)
    private String auctionStartDate;
    
    //@Column(name = "auction_start_time_id", columnDefinition = "int(11) unsigned DEFAULT 1")    
    private int auctionStartTimeId;
    
    //@Column(name = "unix_auction_start", columnDefinition = "int(11) unsigned DEFAULT 1")    
    private long unixAuctionStart;
    
    //@Column(name = "auction_end_date", length = 500)
    private String auctionEndDate;
    
    //@Column(name = "auction_end_time_id", columnDefinition = "int(11) unsigned DEFAULT 1")    
    private int auctionEndTimeId;
    
    //@Column(name = "unix_auction_end", columnDefinition = "int(11) unsigned DEFAULT 1")    
    private long unixAuctionEnd;
    
    //@Column(name = "daily_email_alert", columnDefinition = "boolean DEFAULT 0")
    private boolean dailyEmailAlert;
    
    //@Column(name = "instant_email_alert", columnDefinition = "boolean DEFAULT 0")
    private boolean instantEmailAlert;

    //@Column(name = "title", length = 500)
    private String title;
    
    //@Column(name = "description", length = 1000)
    private String description;
    
    //@Column(name = "first_name", length = 500)
    private String firstName;
    
    //@Column(name = "last_name", length = 500)
    private String lastName;
    
    //@Column(name = "company_name", length = 500)
    private String companyName;
    
    //@Column(name = "phone", length = 50)
    private String phone;
    
    //@Column(name = "base_price")
    private double basePrice;
    
    //@Column(name = "total_bids")    
    private int totalBids;
    
    //@Column(name = "is_featured_ad", columnDefinition = "boolean DEFAULT 0")
    private boolean isFeaturedAd;
    
    //@Column(name = "is_default_bid", columnDefinition = "boolean DEFAULT 1")
    private boolean isDefaultBid;
    
    //@Column(name = "featured_ad_bid", columnDefinition = "double DEFAULT 0")
    private double featuredAdBid;
    
    //@Column(name = "created_on", length = 11, columnDefinition = "int(11) unsigned DEFAULT 0")
    private long createdOn;

    //@Column(name = "modified_on", length = 11, columnDefinition = "int(11) unsigned DEFAULT 0")
    private long modifiedOn;

    public EntityProduct() 
    {
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
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

    public int getTotalBids() {
        return totalBids;
    }

    public void setTotalBids(int totalBids) {
        this.totalBids = totalBids;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public int getSizeId() {
        return sizeId;
    }

    public void setSizeId(int sizeId) {
        this.sizeId = sizeId;
    }

    public String getSizeTitle() {
        return sizeTitle;
    }

    public void setSizeTitle(String sizeTitle) {
        this.sizeTitle = sizeTitle;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeTitle() {
        return typeTitle;
    }

    public void setTypeTitle(String typeTitle) {
        this.typeTitle = typeTitle;
    }

    public int getMinStayId() {
        return minStayId;
    }

    public void setMinStayId(int minStayId) {
        this.minStayId = minStayId;
    }

    public String getMinStayTitle() {
        return minStayTitle;
    }

    public void setMinStayTitle(String minStayTitle) {
        this.minStayTitle = minStayTitle;
    }

    public int getMaxStayId() {
        return maxStayId;
    }

    public void setMaxStayId(int maxStayId) {
        this.maxStayId = maxStayId;
    }

    public String getMaxStayTitle() {
        return maxStayTitle;
    }

    public void setMaxStayTitle(String maxStayTitle) {
        this.maxStayTitle = maxStayTitle;
    }

    public int getSmokingId() {
        return smokingId;
    }

    public void setSmokingId(int smokingId) {
        this.smokingId = smokingId;
    }

    public String getSmokingTitle() {
        return smokingTitle;
    }

    public void setSmokingTitle(String smokingTitle) {
        this.smokingTitle = smokingTitle;
    }

    public int getOccupationId() {
        return occupationId;
    }

    public void setOccupationId(int occupationId) {
        this.occupationId = occupationId;
    }

    public String getOccupationTitle() {
        return occupationTitle;
    }

    public void setOccupationTitle(String occupationTitle) {
        this.occupationTitle = occupationTitle;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public String getPetTitle() {
        return petTitle;
    }

    public void setPetTitle(String petTitle) {
        this.petTitle = petTitle;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getLocationTitle() {
        return locationTitle;
    }

    public void setLocationTitle(String locationTitle) {
        this.locationTitle = locationTitle;
    }

    public String getAmenityIds() {
        return amenityIds;
    }

    public void setAmenityIds(String amenityIds) {
        this.amenityIds = amenityIds;
    }

    public String getAmenityTitles() {
        return amenityTitles;
    }

    public void setAmenityTitles(String amenityTitles) {
        this.amenityTitles = amenityTitles;
    }

    public boolean isOnGoing() {
        return onGoing;
    }

    public void setOnGoing(boolean onGoing) {
        this.onGoing = onGoing;
    }

    public String getAvailabilityIds() {
        return availabilityIds;
    }

    public void setAvailabilityIds(String availabilityIds) {
        this.availabilityIds = availabilityIds;
    }

    public String getAvailabilityTitles() {
        return availabilityTitles;
    }

    public void setAvailabilityTitles(String availabilityTitles) {
        this.availabilityTitles = availabilityTitles;
    }

    public String getAuctionStartDate() {
        return auctionStartDate;
    }

    public void setAuctionStartDate(String auctionStartDate) {
        this.auctionStartDate = auctionStartDate;
    }

    public int getAuctionStartTimeId() {
        return auctionStartTimeId;
    }

    public void setAuctionStartTimeId(int auctionStartTimeId) {
        this.auctionStartTimeId = auctionStartTimeId;
    }

    public long getUnixAuctionStart() {
        return unixAuctionStart;
    }

    public void setUnixAuctionStart(long unixAuctionStart) {
        this.unixAuctionStart = unixAuctionStart;
    }

    public String getAuctionEndDate() {
        return auctionEndDate;
    }

    public void setAuctionEndDate(String auctionEndDate) {
        this.auctionEndDate = auctionEndDate;
    }

    public int getAuctionEndTimeId() {
        return auctionEndTimeId;
    }

    public void setAuctionEndTimeId(int auctionEndTimeId) {
        this.auctionEndTimeId = auctionEndTimeId;
    }

    public long getUnixAuctionEnd() {
        return unixAuctionEnd;
    }

    public void setUnixAuctionEnd(long unixAuctionEnd) {
        this.unixAuctionEnd = unixAuctionEnd;
    }

    public boolean isDailyEmailAlert() {
        return dailyEmailAlert;
    }

    public void setDailyEmailAlert(boolean dailyEmailAlert) {
        this.dailyEmailAlert = dailyEmailAlert;
    }

    public boolean isInstantEmailAlert() {
        return instantEmailAlert;
    }

    public void setInstantEmailAlert(boolean instantEmailAlert) {
        this.instantEmailAlert = instantEmailAlert;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public long getUnixAvailableFrom() {
        return unixAvailableFrom;
    }

    public void setUnixAvailableFrom(long unixAvailableFrom) {
        this.unixAvailableFrom = unixAvailableFrom;
    }

    public String getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(String availableFrom) {
        this.availableFrom = availableFrom;
    }

    public long getUnixAvailableTo() {
        return unixAvailableTo;
    }

    public void setUnixAvailableTo(long unixAvailableTo) {
        this.unixAvailableTo = unixAvailableTo;
    }

    public String getAvailableTo() {
        return availableTo;
    }

    public void setAvailableTo(String availableTo) {
        this.availableTo = availableTo;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isIsFeaturedAd() {
        return isFeaturedAd;
    }

    public void setIsFeaturedAd(boolean isFeaturedAd) {
        this.isFeaturedAd = isFeaturedAd;
    }

    public boolean isIsDefaultBid() {
        return isDefaultBid;
    }

    public void setIsDefaultBid(boolean isDefaultBid) {
        this.isDefaultBid = isDefaultBid;
    }

    public double getFeaturedAdBid() {
        return featuredAdBid;
    }

    public void setFeaturedAdBid(double featuredAdBid) {
        this.featuredAdBid = featuredAdBid;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
    
}
