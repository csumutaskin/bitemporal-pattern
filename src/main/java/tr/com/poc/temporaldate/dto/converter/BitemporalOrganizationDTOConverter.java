package tr.com.poc.temporaldate.dto.converter;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.logging.log4j.Level;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import lombok.extern.log4j.Log4j2;
import tr.com.poc.temporaldate.core.converter.BaseConverter;
import tr.com.poc.temporaldate.dto.OrganizationDTO;
import tr.com.poc.temporaldate.model.BitemporalOrganization;
import tr.com.poc.temporaldate.model.Organization;
import tr.com.poc.temporaldate.util.StringUtils;

@Component
@Log4j2
public class BitemporalOrganizationDTOConverter  implements BaseConverter<BitemporalOrganization, OrganizationDTO>
{
	@Override
	public BitemporalOrganization convertToEntity(OrganizationDTO bd) 
	{		
		if(bd == null)
		{
			log.debug("null object converted to null");
			return null;
		}
		Organization toReturn = new Organization(null, bd.getName(), bd.getShortName(), bd.getFineAmount(), bd.getEarnAmount());
		if(log.isEnabled(Level.DEBUG))
		{
			log.debug(bd.toString() + " object converted to " + toReturn.toString());
		}
		return new BitemporalOrganization(null, bd.getName(), bd.getShortName(), bd.getFineAmount(), bd.getEarnAmount());	
	}

	@Override
	public OrganizationDTO convertToDTO(BitemporalOrganization be) 
	{
		if(be == null)
		{
			log.debug("null object converted to null");
			return null;
		}
		OrganizationDTO toReturn = new OrganizationDTO(be.getName(), be.getShortName(), be.getFineAmount(), be.getEarnAmount());
		if(log.isEnabled(Level.DEBUG))
		{
			log.debug(be.toString() + " object converted to " + toReturn.toString());
		}
		return toReturn;
	}

	@Override
	public Collection<OrganizationDTO> mapListEntityToDTO(Collection<BitemporalOrganization> entityList) 
	{
		if(CollectionUtils.isEmpty(entityList))
		{
			log.debug("empty collection converted to new Arraylist");
			return new ArrayList<>();
		}
		Collection<OrganizationDTO> toReturn = new ArrayList<>();
		for(BitemporalOrganization entity : entityList)
		{
			toReturn.add(convertToDTO(entity));
		}
		if(log.isEnabled(Level.DEBUG))
		{
			log.debug("BitemporalOrganization list converted to OrganizationDTO Arraylist with contents: " + StringUtils.toStringCollection(toReturn));
		}
		return toReturn;
	}

	@Override
	public Collection<BitemporalOrganization> mapListDTOtoEntity(Collection<OrganizationDTO> dtoList) 
	{
		if(CollectionUtils.isEmpty(dtoList))
		{
			log.debug("empty collection converted to new Arraylist");
			return new ArrayList<>();
		}
		Collection<BitemporalOrganization> toReturn = new ArrayList<>();
		for(OrganizationDTO dto : dtoList)
		{
			toReturn.add(convertToEntity(dto));
		}
		if(log.isEnabled(Level.DEBUG))
		{
			log.debug("OrganizationDTO list converted to BitemporalOrganization Arraylist with contents: " + StringUtils.toStringCollection(toReturn));
		}
		return toReturn;
	}
}
