gen-kube-client-src:
	rm -rf kubeclient/generatedSrc/*
	mill openapigen \
	  -s kubeclient/spec/kube-openapi-spec.json \
		-a kubeclient/generatedSrc/example/apis \
		-m kubeclient/generatedSrc/example/models

clean:
	rm -rf kubeclient/generatedSrc/*