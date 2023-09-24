# Spring Trading App :: Trading Agent Invoker

> [!NOTE]
> This is one of many components from
> [Spring Trading App](https://github.com/alexandreroman/sta).

This component takes care of kickstarting trading agents, by periodically calling
the public URL of registered agents.

## Running this component on your workstation

Use this command to run this component on your workstation:

```shell
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

The app is available at http://localhost:8083.

You may want to customize the configuration by registering trading agent URLs.
Open the file `src/main/resources/application-dev.yaml` and edit this section accordingly:

```yaml
app:
  invoker:
    urls:
    - http://localhost:8082
    - http://localhost:9000
```

## Deploying with VMware Tanzu Application Platform

Use this command to deploy this component to your favorite Kubernetes cluster:

```shell
tanzu apps workload apply -f config/workload.yaml
```

The platform will take care of building, testing and deploying this component.

This component also loads some configuration from a
[Git repository](https://github.com/alexandreroman/sta-config).

Run this command to create a Kubernetes `Secret` out of this Git repository,
which will be used by the component at runtime:

```shell
kubectl apply -f config/app-operator
```

Run this command to get deployment status:

```shell
tanzu apps workload get sta-invoker
```
