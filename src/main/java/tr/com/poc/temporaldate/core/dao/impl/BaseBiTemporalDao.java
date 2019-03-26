package tr.com.poc.temporaldate.core.dao.impl;

import static tr.com.poc.temporaldate.util.Constants.END_OF_EPYS;
import static tr.com.poc.temporaldate.util.Constants.ID_SETTER_KEY;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import lombok.extern.log4j.Log4j2;
import tr.com.poc.temporaldate.core.exception.ApplicationException;
import tr.com.poc.temporaldate.core.model.BaseBitemporalEntity;
import tr.com.poc.temporaldate.core.model.BaseEntity;
import tr.com.poc.temporaldate.util.Constants;
import tr.com.poc.temporaldate.util.ExceptionConstants;;

@Component
@SuppressWarnings(value = { "rawtypes", "unchecked"})
@Log4j2
public class BaseBiTemporalDao<E extends BaseBitemporalEntity>
{
	private static final String NO_SINGLE_RESULT_EXC_STRING = "Returning null since NoResultException is thrown and caught";
	private static final String NON_UNIQUE_PERSPECTIVE_EXC_STRING = "Non unique single item returned from a perspective get query"; 
	private static final String NON_UNIQUE_PERSPECTIVE_EXC_PREFIX_STRING = "Non unique single item returned from a perspective get query. Detail: "; 
	private static final String UNEXCPECTED_QUERY_EXC_PREFIX_STRING = "Exception running a query in repository. Detail: ";
	
	@PersistenceContext
	private EntityManager entityManager;

	public EntityManager getEntityManager()
	{
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}
	
	/**
	 * Retrieves currently effective entity from current perspective
	 * @param pk primary key to be searched
	 * @return {@link BaseEntity} object
	 */
	public E getEntity(final Serializable pk)
	{		
		return getEntityForUpdateWithLockMode(pk, null);				
	}
	
	/**
	 * Retrieves currently effective entity from current perspective with pessimistic lock for updating
	 * @param pk primary key to be searched
	 * @return {@link BaseEntity} object
	 */
	public E getEntityForUpdate(final Serializable pk)
	{
		return getEntityForUpdateWithLockMode(pk, LockModeType.PESSIMISTIC_WRITE);
	}
	
	/**
	 * Retrieves entity effective at a given time from current perspective
	 * @param pk primary key to be searched
	 * @param effectiveDate
	 * @return {@link BaseEntity} object
	 */
	public E getEntityAtEffectiveTime(final Serializable pk, Date effectiveDate)
	{
		E toReturn = null;
		Class beType = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		Query selectWithinEffectiveTime = entityManager.createQuery("SELECT * FROM " + beType.getSimpleName() + " E WHERE E.effectiveDateStart <= :effectiveDate and E.effectiveDateEnd >= :effectiveDate and sysdate >= recordDateStart and sysdate <= recordDateEnd and E.id = :id");		
		selectWithinEffectiveTime.setParameter("id", pk);
		selectWithinEffectiveTime.setParameter("effectiveDate", effectiveDate);
		try
		{
			toReturn = (E) selectWithinEffectiveTime.getSingleResult();			
		}
		catch(NoResultException nre)
		{
			log.info(NO_SINGLE_RESULT_EXC_STRING);	
			toReturn = null;
		}
		catch(NonUniqueResultException nure)
		{
			String exceptionMessage = NON_UNIQUE_PERSPECTIVE_EXC_PREFIX_STRING + ExceptionUtils.getStackTrace(nure);	
			log.info(NON_UNIQUE_PERSPECTIVE_EXC_STRING);
			log.error(exceptionMessage);				
			throw new ApplicationException(ExceptionConstants.BITEMPORAL_GET_ENTITY_AT_EFFECTIVE_DATE, ExceptionUtils.getMessage(nure) ,nure);	
		}
		catch(Exception e)
		{
			String exceptionMessage = UNEXCPECTED_QUERY_EXC_PREFIX_STRING + ExceptionUtils.getStackTrace(e);	
			log.error(exceptionMessage);
			throw new ApplicationException(ExceptionConstants.BITEMPORAL_GET_ENTITY_AT_EFFECTIVE_DATE, ExceptionUtils.getMessage(e) ,e);	
		}
		return toReturn;
	}
	
	/**
	 * Retrieves entity effective at a given time from a given perspective
	 * @param pk primary key to be searched
	 * @param effectiveDate actual date for the tuple
	 * @param perspectiveDate from which time line we are looking at the tuples
	 * @return {@link BaseEntity} object
	 */
	public E getEntityAtEffectiveTimeFromPerspectiveTime(final Serializable pk, Date effectiveDate, Date perspectiveDate)//effective at a certain date from a certain perspective (not now)
	{
		E toReturn = null;
		Class beType = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		Query selectWithinEffectiveTimeFromPerspectiveDate = entityManager.createQuery("SELECT * FROM " + beType.getSimpleName() + " E WHERE E.effectiveDateStart <= :effectiveDate and E.effectiveDateEnd >= :effectiveDate and :perspectiveDate >= recordDateStart and :perspectiveDate <= recordDateEnd and id = :id");
		selectWithinEffectiveTimeFromPerspectiveDate.setParameter("effectiveDate", effectiveDate);
		selectWithinEffectiveTimeFromPerspectiveDate.setParameter("perspectiveDate", perspectiveDate);
		selectWithinEffectiveTimeFromPerspectiveDate.setParameter(Constants.ID_COLUMN_KEY, pk);
		try
		{
			toReturn = (E) selectWithinEffectiveTimeFromPerspectiveDate.getSingleResult();			
		}
		catch(NoResultException nre)
		{
			log.info(NO_SINGLE_RESULT_EXC_STRING);	
			toReturn = null;
		}
		catch(NonUniqueResultException nure)
		{
			String exceptionMessage = NON_UNIQUE_PERSPECTIVE_EXC_PREFIX_STRING + ExceptionUtils.getStackTrace(nure);	
			log.info(NON_UNIQUE_PERSPECTIVE_EXC_STRING);
			log.error(exceptionMessage);				
			throw new ApplicationException(ExceptionConstants.BITEMPORAL_GET_ENTITY_AT_EFFECTIVE_TIME_FROM_PERSPECTIVE_TIME, ExceptionUtils.getMessage(nure) ,nure);	
		}
		catch(Exception e)
		{
			String exceptionMessage = UNEXCPECTED_QUERY_EXC_PREFIX_STRING + ExceptionUtils.getStackTrace(e);	
			log.error(exceptionMessage);
			throw new ApplicationException(ExceptionConstants.BITEMPORAL_GET_ENTITY_AT_EFFECTIVE_TIME_FROM_PERSPECTIVE_TIME, ExceptionUtils.getMessage(e) ,e);	
		}
		return toReturn;
	}
	
	/**
	 * Saves or updates (with flush) entity from current perspective
	 * @param id primary key to be updated (if null data will be persisted)
	 * @param baseEntity entity to be saved or updated
	 * @param effectiveStartDate actual start date where the tuple is active
	 * @param effectiveEndDate actual final date where the tuple is active
	 * @return {@link BaseEntity} that is saved or updated
	 */
	public E saveOrUpdateEntityWithFlush(Serializable id, E baseEntity, Date effectiveStartDate, Date effectiveEndDate)
	{		
		return saveOrUpdateEntityWithFlushOption(id, baseEntity, true, effectiveStartDate, effectiveEndDate);
	}
	
	/**
	 * Deletes entity which is effective at the given date
	 * @param id primary key to be updated (if null data will be persisted)
	 * @param effectiveDate actual date for the tuple
	 */
	public void deleteEntityAtEffectiveDate(Serializable id, Date effectiveDate)
	{
		
	}
	
	/**
	 * Deletes entity which is currently effective
	 * @param id primary key to be updated (if null data will be persisted)
	 */
	public void deleteEntityAtEffectiveDate(Serializable id)
	{
		
	}
	
	/**
	 * Deletes all versions????? of entity with the given id
	 * @param id primary key to be updated (if null data will be persisted)
	 */
	public void deleteEntityWithAllVersions(Serializable id)
	{
		
	}
	
	/**
	 * Saves or updates entity from current perspective
	 * @param id primary key to be updated (if null data will be persisted)
	 * @param baseEntity entity to be saved or updated
	 * @param effectiveStartDate actual start date where the tuple is active
	 * @param effectiveEndDate actual final date where the tuple is active
	 * @return {@link BaseEntity} that is saved or updated
	 */
    public E saveorUpdateEntity(Serializable id, E baseEntity, Date effectiveStartDate, Date effectiveEndDate)
    {
    	return saveOrUpdateEntityWithFlushOption(id, baseEntity, false, effectiveStartDate, effectiveEndDate);
    }
    
	/**
	 * Retrieves all entities that intersect the given effective start and end dates
	 * @param pk primary key to be searched
	 * @return {@link BaseEntity} object
	 */
	public Collection<E> getAllEntitiesThatIntersectBeginAndEndDate(final Serializable pk, Date effectiveStartDate, Date effectiveEndDate)
	{
		return null;
	}
	
    /* Saves or Updates entity */
	private E saveOrUpdateEntityWithFlushOption(Serializable pk, E baseEntity, boolean flushNeeded, Date effectiveStartDate, Date effectiveEndDate)
	{	
		//E entityForUpdate = getEntityForUpdate(pk);
		Collection<E> entitiesIntersected = getAllEntitiesThatIntersectBeginAndEndDate(pk, effectiveStartDate, effectiveEndDate);
		if(CollectionUtils.isEmpty(entitiesIntersected))
		{
			entityManager.persist(enrichDateColumns(baseEntity,effectiveStartDate, effectiveEndDate));
		}
		else //update currently found entity
		{
			for(E current:entitiesIntersected)
			{
				//TODO: comparator sort by date begin, then start updating new dates using the given date by end user and also merge the new data 
				//if a tuples both start and end dates match update by merging new data
				//if a tuples at least one date match insert new data and limit the old data times..
			}
		}
		if(flushNeeded)
		{
			entityManager.flush();
		}
		return baseEntity;
	}
	
    /* Saves or Updates entity with pessimistic lock in case of update */
	private E getEntityForUpdateWithLockMode(final Serializable pk, LockModeType lockModeType)
	{
		if(pk == null)
		{
			return null;
		}
		Class beType = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];		
		Query selectCurrentDataUsingEffectiveTimeParameter = entityManager.createQuery("SELECT * FROM " + beType.getSimpleName() + " E WHERE E.effectiveDateStart <= sysdate and E.effectiveDateEnd >= sysdate and sysdate >= recordDateStart and sysdate <= recordDateEnd and E.id = :id");		
		selectCurrentDataUsingEffectiveTimeParameter.setParameter(Constants.ID_COLUMN_KEY, pk);
		if(lockModeType != null)
		{
			selectCurrentDataUsingEffectiveTimeParameter.setLockMode(lockModeType);
			return (E) selectCurrentDataUsingEffectiveTimeParameter.getSingleResult();
		}
		else
		{
			return (E) selectCurrentDataUsingEffectiveTimeParameter.getSingleResult();
		}		
	}	
	
	@SuppressWarnings("unused")
	private E setIdusingReflection(E baseEntity, Serializable id) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException
	{
		Method method = null;		
		try
		{
			Class beType = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			method = beType.getMethod(ID_SETTER_KEY, BigDecimal.class);
			method.invoke(baseEntity, id);
		}
		catch(Exception e)
		{
			String errorMessage = "Can not invoke setId() using reflection on object: " + baseEntity + ". Detail: " + ExceptionUtils.getStackTrace(e);
			log.error(errorMessage);
			log.info("Can not invoke setId() using reflection on object: " + baseEntity + " See errog log for details...");					 
			throw e;
		}
		return baseEntity;
	}
	
	/* Sets an effective (actual) start date and end date for the current tuple */
	private E enrichDateColumns(E baseEntity, Date effectiveStartDate, Date effectiveEndDate)
	{
		baseEntity.setRecordDateStart(new Date());//TODO: Audit create date alinmali...
		baseEntity.setRecordDateEnd(END_OF_EPYS);
		baseEntity.setEffectiveDateStart(effectiveStartDate);
		baseEntity.setEffectiveDateEnd(effectiveEndDate);
		return baseEntity;
	}
}