package air.kanna.kindlesync.scan.filter.senior;

public class FileScanCondition {
	private String matchName = "";
	private boolean isRegex = false;
	private boolean isIgnoreCase = false;
	private boolean isAcceptRejectContinue = false;

	public String getMatchName() {
		return matchName;
	}

	public void setMatchName(String matchName) {
		this.matchName = matchName;
	}

	public boolean isRegex() {
		return isRegex;
	}

	public void setRegex(boolean isRegex) {
		this.isRegex = isRegex;
	}

	public boolean isAcceptRejectContinue() {
		return isAcceptRejectContinue;
	}

	public void setAcceptRejectContinue(boolean isAcceptRejectContinue) {
		this.isAcceptRejectContinue = isAcceptRejectContinue;
	}

	public boolean isIgnoreCase() {
		return isIgnoreCase;
	}

	public void setIgnoreCase(boolean isIgnoreCase) {
		this.isIgnoreCase = isIgnoreCase;
	}
}
