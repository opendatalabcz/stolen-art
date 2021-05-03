# Usage

## REST API

The server uses Django REST framework, which offers [Browsable API](https://www.django-rest-framework.org/topics/browsable-api/).
Two main endpoints were implemented:

1. **Paintings**
   - for listing all paintings in the database
   - for uploading a new painting
2. **Search**
   - for searching the database by content

To browse (and make requests) using a browser:
1. Start the server.
```console
admin@admin:~$ docker-compose up
``` 
2. Open the [API root](http://127.0.0.1:8000/api/painting/).
3. Navigate to a any endpoint and fill in a form to make a request. 


## Admin commands

### Uploading paintings from directory

To upload a whole directory of paintings (i.e., initial data):

1. Move the folder with the painting to *server/datasets/* directory.
2. Navigate to the *server* directory.
3. Run this command (replace your-dir-name with the name of your directory):
```console
admin@admin:~$ docker-compose run --rm app sh -c "python3 manage.py load_paintings_from_dir your-dir-name"
```  
4. Wait for confirmation that the paintings were successfuly uploaded.


### Flushing the database

If you want to erase all data from the database run the following command: 

```console
admin@admin:~$ docker-compose run --rm app sh -c "python3 manage.py flush"
```

