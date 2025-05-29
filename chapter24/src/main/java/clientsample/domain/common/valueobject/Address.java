package clientsample.domain.common.valueobject;

public class Address {
    private String country;
    private String province;
    private String city;
    private String district;
    private String detail;

    public Address(String country
        , String province
        , String city
        , String district
        , String detail
    ) {
        this.country = country;
        this.province = province;
        this.city = city;
        this.district = district;
        this.detail = detail;
    }

    public String getCountry() {
        return country;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getDetail() {
        return detail;
    }
}
