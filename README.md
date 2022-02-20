## Description

A small REST-service to manage a store's inventory, built as a small "look I can write code" assignment for a potential
client.

Due to the time-constraints it's obviously missing some stuff (see below), but it's currently functional.

The application keeps track of stores and the amount of stuff in their inventory. It's of course possible to request a
store's current inventory, add items to it, or update the amount of items in it.

It's also possible to reserve an item in a store for a customer; then that item will reserved for 5 minutes, during
which time it's guaranteed to not leave the inventory (e.g. by updating it's the amount).

To run:

```shell
./gradlew clean bootRun
```

Note that the application starts with some random stores and inventory already added.

To run the tests:

```shell
./gradlew test
```

## API

Get stores:

```shell
curl -X GET -H "Content-Type: application/json" \
    http://localhost:8080/stores
```

Get item types:

```shell
curl -X GET -H "Content-Type: application/json" \
    http://localhost:8080/items
```

Create a new item type:

```shell
curl -X POST -H "Content-Type: application/json" \
    -d '{"name": [NAME] }' \
    http://localhost:8080/items
```

Get a store's inventory:

```shell
curl -X GET -H "Content-Type: application/json" \
    http://localhost:8080/stores/[STORE ID]/inventory
```

Update a store's inventory with an existing item type:

```shell
curl -X PUT -H "Content-Type: application/json" \
    -d '{"id" : [ITEM TYPE ID], "total": [AMOUNT]}' \
    http://localhost:8080/stores/1/inventory
```

Update a store's inventory with a new item type:

```shell
curl -X PUT -H "Content-Type: application/json" \
    -d '{"name" : [NAME OF ITEM] , "total": [AMOUNT]}' \
    http://localhost:8080/stores/1/inventory
```

Make a reservation:

```shell
curl -X POST -H "Content-Type: application/json" \
    -d '{"storeId": [STORE ID], "itemTypeId": [ITEM TYPE ID], "customerName": [NAME]}' \
    http://localhost:8080/reservation
```

## TODO

Stuff I still wanted to implement, but couldn't cause I ran out of time:

* Talk to an actual database; the [MemoryDataSource] works within the scope of the project, but can easily be replaced
  by an actual (in-memory or not) database.
* Add a webbased frontend; the API was designed with this in mind, so it shouldn't be that hard.
* Better documentation of the API; easily done by adding Swagger.
