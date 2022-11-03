select PFdataset.patientcode, PFDATASETLINK.key from PFdataset,PFDATASETLINK
where PFDATASETLINK.IDPFDATASET = PFdataset.IDPFDATASET
and DATACONTEXTABBR='SSCS_0001' 
and key like 'ZAS%' and value = 1
order by patientcode