## jpcontentscheduler

This plugin is used to manage contents temporal windows based on the content's dates.

It is possible to set up rules that:

 - will move an expired content to a public archive
 - will unpublish the expired content
 - will automatically publish the content
 - will send email notifications to a list of users


### General Settings:

- `Active`
 boolean used to enable or disable the plugin

- `SiteCode`
 configuration parameter related to the `clustering` plugin 

- `Global category`
 mandatory category code that each content will be assigned to when suspended or moved to a generic archive not belonging to a specific category 

- `Content replacement`
 content identifier that will replace the archived/unpublished content

- `Content model replacement`
 model identifier that will replace the archived/unpublished content

### Email Settings:

These settings are used to manage how email notifications are being sent


### Users Settings:

Used to define bindings between users and content types.
It is possible to specify in a very granular way which user should receive emails for each type of content.

### Content Types Settings:

- `start date attribute`
 this mandatory parameter defines the mapping of the field used for automatic publishing.

- `end date attribute` 
 this mandatory parameter defines the mapping of the field used to determine whether or not the content has expired.

- `Content replace id`
 this optional parameter overrides the `General Settings` `Content replacement` setting.
 
- `Content replace model id`
 this optional parameter overrides the `General Settings` `Content model replacement` setting.

- `Suspend`
 mandatory boolean to define the behaviour of an expired content: `true` unpublishes the content while `false` archives it. 
 