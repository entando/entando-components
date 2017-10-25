package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.common.entity.model.IApsEntity;

/**
 * This class generates a valid Entando prototype code given the EXISTING
 * list of prototypes already present in the system
 * @author entando
 *
 */
public class EntityNaming {

	/**
	 * Generate the first entity code available given the list of the existing ones
	 * @param existingPrototypes
	 * @return
	 */
	public static String generateEntityName(List<String> existingPrototypes) {
		String code = null;
		int min = 0;
		
		for (int idx = 0; idx < existingPrototypes.size(); idx++) {
			code = existingPrototypes.get(idx);
			int cur = entity2int(code);
			if (cur < min) {
				min = cur;
			}
		}
		do {
			code = int2entity(min++);
		} while (existingPrototypes.contains(code));
		return code;
	}

	/**
	 * Generate the first entity code available given the list of the existing ones
	 * @param existingPrototypes
	 * @return
	 */
	public static String generateEntityName(Map<String, IApsEntity> existingPrototypes) {
		List<String> list = new ArrayList<String>(existingPrototypes.keySet());

		return generateEntityName(list);
	}

	/**
	 * Normalize the code of the entity
	 * @param code
	 * @return
	 * @throws Throwable
	 */
	public static String entityNameNormalize(String code) throws Throwable  {
		String res = null;

		if (null == code) {
			res = int2entity(0);
		} else if (code.length() < 4) {
			int tmp = entity2int(code);
			res = int2entity(tmp);
		} else {
			throw new RuntimeException("Invalid code detected");
		}
		return res;
	}
	
	private static int entity2int(String code) {
		int res = 0;
		
		code = new StringBuilder(code.toLowerCase()).reverse().toString();
		for (int idx = 0; idx < code.length(); idx++) {
			res += (((int)code.charAt(idx)) - base) * Math.pow(alphabet.length, idx);
		}
		return res;
	}

	private static String int2entity(int value) {
		StringBuilder code = new StringBuilder();
		int digit = 0;

		do {
			digit = value % alphabet.length;
			value /= alphabet.length;
			code.append(String.valueOf(alphabet[digit]));
		} while (value != 0);
		while (code.length() < 3) {
			code.append(alphabet[0]);
		}
		return code.reverse().toString().toUpperCase();
	}
	
	// High capacity, number and letters mixed!
	// private final static char alphabet[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	
	// standard, letters only
	private final static char alphabet[] = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	
	// HEX, useful for testing purposes
	// private final static char alphabet[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
	
	private final static int base = (int)alphabet[0];
}
