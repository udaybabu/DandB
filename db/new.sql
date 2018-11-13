ALTER TABLE `team` ADD COLUMN `status` INT(1) default 1;
###########################################################

DROP TABLE `qf_dnb_db`.`alert`, `qf_dnb_db`.`alert_log`, `qf_dnb_db`.`alert_type`, 
`qf_dnb_db`.`assignment_group`, `qf_dnb_db`.`assignment_type`, `qf_dnb_db`.`associate_workflow`, 
`qf_dnb_db`.`attendance`, `qf_dnb_db`.`audio`, `qf_dnb_db`.`audit_customer`, `qf_dnb_db`.`audit_user`, 
`qf_dnb_db`.`bill_info`, `qf_dnb_db`.`bupc`, `qf_dnb_db`.`cm_files`, `qf_dnb_db`.`cm_files_opened_time`, 
`qf_dnb_db`.`cm_folder_strcture`, `qf_dnb_db`.`customer_module_change_log`, `qf_dnb_db`.`customer_usage`, 
`qf_dnb_db`.`customer_usage_detail`, `qf_dnb_db`.`customize_data`, `qf_dnb_db`.`customize_view`, 
`qf_dnb_db`.`custom_report`, `qf_dnb_db`.`data_type`, `qf_dnb_db`.`data_type_key`, 
`qf_dnb_db`.`db_script_log`, `qf_dnb_db`.`def_dtr`;

ALTER TABLE `qf_dnb_db`.`def_dynafield` 
DROP FOREIGN KEY `def_dynafield_ibfk_2`;
ALTER TABLE `qf_dnb_db`.`def_dynafield` 
DROP INDEX `def_dynafield_ibfk_2` ;

DROP TABLE `qf_dnb_db`.`def_phone_configuration`, `qf_dnb_db`.`def_web_configuration`, 
`qf_dnb_db`.`district`, `qf_dnb_db`.`dynafield_locale`, `qf_dnb_db`.`entity_field`, 
`qf_dnb_db`.`event_locale`, `qf_dnb_db`.`geofence`, `qf_dnb_db`.`glenmark_email_check_scheduler`, 
`qf_dnb_db`.`glenmark_message`, `qf_dnb_db`.`group_details`, `qf_dnb_db`.`hierarchy`, 
`qf_dnb_db`.`hierarchy_type`, `qf_dnb_db`.`ic_customer_data`, `qf_dnb_db`.`ic_customer_feedback`, 
`qf_dnb_db`.`ic_new_customer`, `qf_dnb_db`.`ic_tracking`, `qf_dnb_db`.`logo`, `qf_dnb_db`.`lov`, 
`qf_dnb_db`.`mapping`, `qf_dnb_db`.`message_log`, `qf_dnb_db`.`messaging`, `qf_dnb_db`.`module_details`, 
`qf_dnb_db`.`module_price`, `qf_dnb_db`.`old_pn33_file_backup`, `qf_dnb_db`.`orix_trip_details`, 
`qf_dnb_db`.`partner`, `qf_dnb_db`.`payment`, `qf_dnb_db`.`payment_detail`, `qf_dnb_db`.`payment_type`, 
`qf_dnb_db`.`pdf_data`, `qf_dnb_db`.`phone_model`, `qf_dnb_db`.`picture`, `qf_dnb_db`.`query_data`, 
`qf_dnb_db`.`route`, `qf_dnb_db`.`routeAssociation`, `qf_dnb_db`.`route_detail`, `qf_dnb_db`.`service_pn33`, 
`qf_dnb_db`.`service_rfile`, `qf_dnb_db`.`smart_job_zone`, `qf_dnb_db`.`state`, `qf_dnb_db`.`sub_customer`, 
`qf_dnb_db`.`subcustomerAssociation`, `qf_dnb_db`.`subcustomer_detail`, `qf_dnb_db`.`subcustomer_field_map`, 
`qf_dnb_db`.`subscription`, `qf_dnb_db`.`taluk`, `qf_dnb_db`.`tblessellog`, `qf_dnb_db`.`tblfilelog`, 
`qf_dnb_db`.`tblprocesslog`, `qf_dnb_db`.`temp_mahindra`, `qf_dnb_db`.`tomcat_start_end_log`, 
`qf_dnb_db`.`traceTime`, `qf_dnb_db`.`upload_file_log`, `qf_dnb_db`.`user_table_change_log`, 
`qf_dnb_db`.`video`, `qf_dnb_db`.`village`, `qf_dnb_db`.`web_configuration`, 
`qf_dnb_db`.`workflow_event_data`, `qf_dnb_db`.`workflow_event_instance`, `qf_dnb_db`.`workflow_instance`, 
`qf_dnb_db`.`workflow_locale`, `qf_dnb_db`.`workflow_updated_event_data`, `qf_dnb_db`.`def_dynafield`, 
`qf_dnb_db`.`def_event`;

DROP TABLE `qf_dnb_db`.`dynafield_type`, `qf_dnb_db`.`entity`, `qf_dnb_db`.`module`;

ALTER TABLE `qf_dnb_db`.`user` 
DROP INDEX `locale_id` ;

ALTER TABLE `qf_dnb_db`.`super_user` 
DROP FOREIGN KEY `super_user_ibfk_4`;
ALTER TABLE `qf_dnb_db`.`super_user` 
DROP INDEX `locale_id` ;

DROP TABLE `qf_dnb_db`.`locale`;

COLUMN:::::::::
=======================


ALTER TABLE `qf_dnb_db`.`customer` 
DROP COLUMN `is_bq_enabled`,
DROP COLUMN `query_box_ip`,
DROP COLUMN `event_impl`,
DROP COLUMN `module_id`,
DROP COLUMN `disabled_from_date`,
DROP COLUMN `disabled_date`,
DROP COLUMN `partner_id`,
DROP COLUMN `use_partner_setting`,
DROP COLUMN `carrier_account_number`,
DROP COLUMN `accepted_aggrement`;

ALTER TABLE `qf_dnb_db`.`team` 
DROP COLUMN `tracking_end_time`,
DROP COLUMN `tracking_start_time`;

ALTER TABLE `qf_dnb_db`.`user` 
DROP COLUMN `device_type`,
DROP COLUMN `device_type_id`,
DROP COLUMN `tracking_end_time`,
DROP COLUMN `tracking_start_time`,
DROP COLUMN `dyna_id`,
DROP COLUMN `village_id`,
DROP COLUMN `taluk_id`,
DROP COLUMN `district_id`,
DROP COLUMN `state_id`,
DROP COLUMN `deleteCheck_id`,
DROP COLUMN `phoneNumber`,
DROP COLUMN `disabled_from_date`,
DROP COLUMN `partner_id`,
DROP COLUMN `external_id`,
DROP COLUMN `r4`,
DROP COLUMN `r3`,
DROP COLUMN `r2`,
DROP COLUMN `r1`,
DROP COLUMN `locale_id`,
DROP COLUMN `ip_address`;


ALTER TABLE `qf_dnb_db`.`phone_configuration` 
DROP COLUMN `partner_id`;

ALTER TABLE `qf_dnb_db`.`def_workflow` 
DROP COLUMN `partner_id`;


drop table workflow_media;

ALTER TABLE `qf_dnb_db`.`user` 
CHANGE COLUMN `app_version_update_time` `app_version_update_time` TIMESTAMP NOT NULL DEFAULT '2000-01-01 10:10:00';

alter table user change column `imei` `imei` varchar(200) DEFAULT NULL;



  

