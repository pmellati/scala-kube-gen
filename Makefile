kube-client-gen-src:
	rm -rf kubeclient/generatedSrc/*
	mill openapigen \
	  -s kubeclient/spec/v1.16.4/swagger.json \
		-p "kubeclient" \
		-a kubeclient/generatedSrc/kubeclient/apis \
		-m kubeclient/generatedSrc/kubeclient/models

clean:
	rm -rf kubeclient/generatedSrc/*