package com.nis.domain.restful;


import com.nis.domain.BaseEntity;
/**
 * 
 * @ClassName: IpLocationPublic.java 
 * @Description: TODO
 * @author (wx)
 * @date 2016年9月9日 上午9:29:56
 * @version V1.0
 */
public class IpLocationPublic extends BaseEntity<IpLocationPublic>{
    /**
	 * @Fields serialVersionUID:TODO（用一句话描述这个变量表示什么）
	 *
	 * @since 1.0.0
	 */
	
	private static final long serialVersionUID = -4375875160095552557L;

	private Long id;

    private String ipStart;

    private String ipEnd;

    private Long ipStartNum;

    private Long ipEndNum;

    private String continent;

    private String country;

    private String province;

    private String city;

    private String district;

    private String description;

    private String isp;

    private String areaCode;

    private String countryEnglish;

    private String countryCode;

    private String longitude;

    private String latitude;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIpStart() {
        return ipStart;
    }

    public void setIpStart(String ipStart) {
        this.ipStart = ipStart == null ? null : ipStart.trim();
    }

    public String getIpEnd() {
        return ipEnd;
    }

    public void setIpEnd(String ipEnd) {
        this.ipEnd = ipEnd == null ? null : ipEnd.trim();
    }

    public Long getIpStartNum() {
        return ipStartNum;
    }

    public void setIpStartNum(Long ipStartNum) {
        this.ipStartNum = ipStartNum;
    }

    public Long getIpEndNum() {
        return ipEndNum;
    }

    public void setIpEndNum(Long ipEndNum) {
        this.ipEndNum = ipEndNum;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent == null ? null : continent.trim();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district == null ? null : district.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp == null ? null : isp.trim();
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode == null ? null : areaCode.trim();
    }

    public String getCountryEnglish() {
        return countryEnglish;
    }

    public void setCountryEnglish(String countryEnglish) {
        this.countryEnglish = countryEnglish == null ? null : countryEnglish.trim();
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode == null ? null : countryCode.trim();
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude == null ? null : longitude.trim();
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? null : latitude.trim();
    }
}