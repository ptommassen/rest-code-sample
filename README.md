Get a store's inventory:

```shell
curl -X GET -H "Content-Type: application/json" \
    http://localhost:8080/stores/1/inventory
```

Make a reservation:

```shell
curl -X POST -H "Content-Type: application/json" \
    -d '{"storeId": 1, "itemTypeId":1, "customerName": "Bertje"}' \
    http://localhost:8080/reservation
```