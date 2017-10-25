/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie;

import java.util.List;
import com.agiletec.aps.system.common.FieldSearchFilter;

public interface IKieFormOverrideDAO {

	public List<Integer> searchKieFormOverrides(FieldSearchFilter[] filters);

	public KieFormOverride loadKieFormOverride(int id);

	public List<Integer> loadKieFormOverrides();

	public void removeKieFormOverride(int id);

	public void updateKieFormOverride(KieFormOverride kieFormOverride);

	public void insertKieFormOverride(KieFormOverride kieFormOverride);


}