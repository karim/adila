## 2016-08-14

This is the first public release. `Device` class has the following fields and methods.

#### Fields

- `FOUND` Set to **true** if the device class was found in the database.
- `MANUFACTURER` The manufacturer of the device.
- `NAME` The user-friendly name of the device.
- `FULL_NAME` The full name of the device, including the manufacturer. (`MANUFACTURER + ' ' + NAME`)
- `SERIES` The series of the device, if any.





#### Methods

- `info()` Returns the full information about the device as a JSON string.

  ```json
  {
    "manufacturer": "Samsung",
    "name": "Galaxy Tab 3 7.0",
    "series": "Galaxy Tab"
  }
  ```
