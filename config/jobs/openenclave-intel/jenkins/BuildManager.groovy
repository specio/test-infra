
def getDockerImage = ["SGX1-FLC": "ImagedoSGXFLC", "SGX1-FLC-KSS": "ImagedoKSS", "SGX1": "ImagedoSGX1"]

public void BuildAndTest() {
	if (isUnix()) {
		for (key in getDockerImage.keySet()) {
			println(key)
			println(getDockerImage[key])
		}
	}
}
return this
