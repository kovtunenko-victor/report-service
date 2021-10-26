echo "Test message"
podman stop report-service
podman rm report-service
podman rmi report-service:latest
echo "Test message1"
podman build -t report-service:latest .
echo "Test message2"
podman run -d \
-e START_POOL_SIZE=10 \
-e MAX_POOL_SIZE=20 \
-e THREAD_POOL_QUEUE_CAPACITY=50 \
-e SERVICE_DB_HOST=localhost:5432 \
-e SERVICE_DB_NAME=postgres \
-e SERVICE_DB_USER=postgres \
-e SERVICE_DB_PASS=rsdc_pwd01 \
-e TARGET_DB_HOST=db-4way03.open.ru:1526 \
-e TARGET_DB_SID=prod \
-e TARGET_DB_USER=reporter \
-e TARGET_DB_PASS=reporter2015 \
-v /home/podmanusr/open_shares/prod/logs:/home/logs \
-v /home/podmanusr/open_shares/prod/reports-template:/home/reports-template \
-v /home/podmanusr/open_shares/prod/reports:/home/reports \
--restart=always --pod=way4service --name=report-service localhost/report-service
