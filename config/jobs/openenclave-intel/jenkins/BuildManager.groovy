
def getDockerImage = ["SGX1-FLC": "ImagedoSGXFLC", "SGX1-FLC-KSS": "ImagedoKSS", "SGX1": "ImagedoSGX1"]


public void buildAndTest() {
	if (isUnix()) {
		sh  """
			echo "starrt"
		"""
		def colors = ["SGX1-FLC": "oetools-full-18.04", "SGX1-FLC-KSS": "oetools-full-18.04-KSS", "SGX1": "oetools-sgx1-llc-full-18.04"]
		for (key in colors.keySet().sort()) {
			ALA = colors[key]
			sh  """
				echo "srala ${ALA}"
			"""
		}
		sh  """
			echo "mett"
		"""
	}
}
return this
