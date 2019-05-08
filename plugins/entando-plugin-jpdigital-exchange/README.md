# Digital Exchange client plugin

## Client secret encryption configuration

When you add a DE configuration the client secret is encrypted before storing it into the database.

You can overwrite the default encryption key adding the following file to your Entando instance: `src/main/webapp/WEB-INF/plugins/jpdigital-exchange/apsadmin/security.properties`.

Then you can define your key:

    key=mykey

Please note that if you change the key the old client secret can't be decrypted anymore and must be inserted again.
