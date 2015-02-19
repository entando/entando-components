INSERT INTO sysconfig(
            version, item, descr, config)
    VALUES ('test', 'jpsharedocs_config', 'Share documents configuration', '<?xml version="1.0" encoding="UTF-8"?>
<config>
   <removal>
      <attachmentDeletion>true</attachmentDeletion>
      <deletion>false</deletion>
   </removal>
   <tmpWorkingPath>/tmp/path</tmpWorkingPath>
</config>');