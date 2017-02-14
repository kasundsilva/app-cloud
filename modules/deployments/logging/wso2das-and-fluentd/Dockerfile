FROM fluent/fluentd:v0.14-latest
MAINTAINER WSO2 Cloud Team <cloud@wso2.com>

#USER fluent
WORKDIR /home/fluent
ENV PATH /home/fluent/.gem/ruby/2.3.0/bin:$PATH

USER root
RUN apk --no-cache --update add sudo build-base ruby-dev && \
    sudo -u fluent gem install fluent-plugin-kubernetes_metadata_filter:0.26.0 fluent-plugin-record-reformer fluent-plugin-out-http-ext:0.1.9 && \
    rm -rf /home/fluent/.gem/ruby/2.3.0/cache/*.gem && sudo -u fluent gem sources -c && \
    apk del sudo build-base ruby-dev && rm -rf /var/cache/apk/*

#USER fluent

#Add fluentd configuration file
ADD fluent.conf /fluentd/etc/

#Copy certs required to communicate with k8s API Server
COPY certs/ca.pem /fluentd/etc/certs/ca.pem
COPY certs/admin-key.pem /fluentd/etc/certs/admin-key.pem
COPY certs/admin.pem /fluentd/etc/certs/admin.pem

CMD fluentd -c /fluentd/etc/fluent.conf -p /fluentd/plugins $FLUENTD_OPT

