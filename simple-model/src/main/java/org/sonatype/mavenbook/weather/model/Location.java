package org.sonatype.mavenbook.weather.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
	@NamedQuery(name="Location.uniqueByWoeid", query="from Location l where l.woeid = :woeid")
})
public class Location {
    
    @Id
    private String woeid;

    private String city;
    private String region;
    private String country;

    public Location() {}

    public String getWoeid() { return woeid; }
    public void setWoeid(String woeid) { this.woeid = woeid; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getRegion() {	return region; }
    public void setRegion(String region) { this.region = region; }
    
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

}