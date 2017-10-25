/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.KieFormOverride;


public class KieFormOverrideChangedEvent extends ApsEvent {

	@Override
	public void notify(IManager srv) {
		((KieFormOverrideChangedObserver) srv).updateFromKieFormOverrideChanged(this);
	}

	@Override
	public Class getObserverInterface() {
		return KieFormOverrideChangedObserver.class;
	}

	public int getOperationCode() {
		return _operationCode;
	}
	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}

	public KieFormOverride getKieFormOverride() {
		return _kieFormOverride;
	}
	public void setKieFormOverride(KieFormOverride kieFormOverride) {
		this._kieFormOverride = kieFormOverride;
	}

	private KieFormOverride _kieFormOverride;
	private int _operationCode;

	public static final int INSERT_OPERATION_CODE = 1;
	public static final int REMOVE_OPERATION_CODE = 2;
	public static final int UPDATE_OPERATION_CODE = 3;

}
