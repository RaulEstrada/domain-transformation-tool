/**
 * generated by Xtext 2.9.0
 */
package org.xtext.example.tfg.myDsl;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>ENUMERATION</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.xtext.example.tfg.myDsl.ENUMERATION#getEnumelements <em>Enumelements</em>}</li>
 * </ul>
 *
 * @see org.xtext.example.tfg.myDsl.MyDslPackage#getENUMERATION()
 * @model
 * @generated
 */
public interface ENUMERATION extends ELEMENT
{
  /**
   * Returns the value of the '<em><b>Enumelements</b></em>' attribute list.
   * The list contents are of type {@link java.lang.String}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Enumelements</em>' attribute list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Enumelements</em>' attribute list.
   * @see org.xtext.example.tfg.myDsl.MyDslPackage#getENUMERATION_Enumelements()
   * @model unique="false"
   * @generated
   */
  EList<String> getEnumelements();

} // ENUMERATION
