package org.sonatype.mavenbook.weather.persist;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import org.sonatype.mavenbook.weather.model.Location;

public class LocationDAO extends HibernateDaoSupport {

    public LocationDAO() {}

    public Location findByWoeid(final String woeid) {
    	return (Location) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) {
				Query query = getSession().getNamedQuery("Location.uniqueByWoeid");
				query.setString("woeid", woeid);
				return (Location) query.uniqueResult();
			}
		});
    }
    
    @SuppressWarnings("unchecked")
	public List<Location> all() {
    	return new ArrayList<Location>( getHibernateTemplate().loadAll(Location.class) );
    }

}