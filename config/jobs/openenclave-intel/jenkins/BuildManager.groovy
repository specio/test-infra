
def getDockerImage = ["SGX1-FLC": "ImagedoSGXFLC", "SGX1-FLC-KSS": "ImagedoKSS", "SGX1": "ImagedoSGX1"]

public void buildAndTest() {
	if (isUnix()) {
		sh  """
			echo "starrt"
		"""
		def PLATF=getDockerImage["SGX_FLC"]
		sh  """
			echo ${PLATF}
		"""
		sh  """
			echo "mett"
		"""
	}
}
return this
