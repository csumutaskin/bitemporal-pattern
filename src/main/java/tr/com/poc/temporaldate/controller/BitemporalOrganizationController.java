package tr.com.poc.temporaldate.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;
import tr.com.poc.temporaldate.core.util.comparator.DateUtils;
import tr.com.poc.temporaldate.dto.BitemporalOrganizationDTO;
import tr.com.poc.temporaldate.service.BitemporalOrganizationService;

/**
 * Sample organization controller having bi-temporal data
 * 
 * @author umut
 */
@RestController
@Log4j2
@RequestMapping(value = "/bitemporal-organization")
public class BitemporalOrganizationController 
{
	@Autowired
	private BitemporalOrganizationService bitemporalOrganizationService;
	
	@GetMapping(value = "/getAll" , produces= {"application/json"})
	public ResponseEntity<List<BitemporalOrganizationDTO>> getOrganizationList()
	{		
		return new ResponseEntity<>(bitemporalOrganizationService.getAllOrganizations(new Date()), HttpStatus.OK);
	}
	
	@PutMapping(value = "/update/{id}" , consumes = {"application/json"}, produces= {"application/json"})
	public ResponseEntity<Boolean> updateOrganization(@PathVariable BigDecimal id, @RequestBody BitemporalOrganizationDTO toUpdate)
	{		
		return new ResponseEntity<>(bitemporalOrganizationService.updateOrganization(id, toUpdate, new Date(), DateUtils.END_OF_SOFTWARE), HttpStatus.OK);
	}
	
	@PostMapping(value = "/save" , consumes = {"application/json"}, produces= {"application/json"})
	public ResponseEntity<Boolean> saveOrganization(@RequestBody BitemporalOrganizationDTO toSave)
	{	
		BigDecimal organizationId = bitemporalOrganizationService.saveOrganization(toSave);
		log.debug("Organization created with id: {}", organizationId);
		return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<Boolean> deleteOrganization(@PathVariable BigDecimal id)
	{		
		return new ResponseEntity<>(bitemporalOrganizationService.deleteOrganization(id), HttpStatus.OK);
	}
}
