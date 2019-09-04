#!/bin/bash
mvn --batch-mode verify sonar:sonar \
    -Dsonar.gitlab.api_version=v3 \
	-Dsonar.host.url=http://114.55.179.182:9008 \
	-Dsonar.login=developer \
	-Dsonar.password=zxy123456 \
	-Dsonar.issuesReport.html.enable=true \
	-Dsonar.analysis.mode=publish \
	-Dmaven.test.skip=true

if [ $? -eq 0 ]; then
    echo "sonarqube code-analyze over."
fi
