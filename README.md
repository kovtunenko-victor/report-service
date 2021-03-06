# report-service это REST сервис для построения отчетов jasper

основной причиной разработки сервиса было не возможность настроить основную систему, таким 
образом, что бы можно было запустить "объемный" отчет на jasper.

report-service является компонентом системы "Построения отчетов" реализующий непосредственно 
построение отчетов jasper. (в состав системы так же входит сервис report-service-balancer, 
который еще нужно разработать)

report-service сервис ориентирован на запуск в контейнере docker/podman

в состав сервиса входит: сервис report-service, база данных конфигурации сервиса postgres, 
база данных основной системы

в сервисе реализовано построение отчета в пуле потоков заданного объема. при попытке выполнить 
отчет сверх этого лимита сервис вернет ошибку 503

сервис позволяет построить отчет как синхронно, так и асинхронно

у сервиса есть документация swagger описывающая интерфейс сервиса http://server_name:server_port/swagger-ui.html


### кратко о сервисе

реализовано 2 интерфейса:

* асинхронный http://server_name:server_port/report-service/report/execute-async/{reportId}
* синхронный  http://server_name:server_port/report-service/report/execute/{reportId}

оба интерфейса ожидают на вход сообщение с RequestBody в формате JSON (Map<String,Object> параметров jasper отчета и путь к выгружаемому файлу):
```json
{
"exportFilePath": "/home/reports/testreports/test.xlsx"
    , "properties": {
        , "reportProp1": "propValue1"
        , "reportProp2": 123
        , "reportProp3": 10.01
        , "reportProp3": { "prop1": "val1", "prop2": "val2" }
    }
}
```
каждый отчет, запускаемый сервисом будет выполнен в отдельном потоке пула потоков. настройки пула настраиваются при запуске сервиса.
если в пуле нет доступного потока сервис вернет ответ со статусом 503.

### кратко о бд конфигурации

app-openway02.open.ru:9981 бд postgres логин postgres пасс rsdc_pwd01 

в базе 2 таблицы

* reports - настройки доступных отчетов для построения и их настройки
* virtualaizer_properties - настройки jasper virtualaizer.

### кратко о сборке и развертывании сервиса

* собрать проект с помощью maven (например командой install)
* в папке cmd выполнить файл deploy_to_app-openway02.bat
* зайти на app-openway02 под podmanusr(пасс как на схеме reporter) и выполнить ~/way4service/deploy.sh




 



