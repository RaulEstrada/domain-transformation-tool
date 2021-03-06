/**
 * generated by Xtext 2.9.0
 */
package org.xtext.example.tfg.myDsl;

import org.eclipse.xtext.common.types.JvmTypeReference;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>ANNOTATED ATTRIBUTE</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.xtext.example.tfg.myDsl.ANNOTATED_ATTRIBUTE#getAnnotation <em>Annotation</em>}</li>
 *   <li>{@link org.xtext.example.tfg.myDsl.ANNOTATED_ATTRIBUTE#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see org.xtext.example.tfg.myDsl.MyDslPackage#getANNOTATED_ATTRIBUTE()
 * @model
 * @generated
 */
public interface ANNOTATED_ATTRIBUTE extends ENTITYELEMENT
{
  /**
   * Returns the value of the '<em><b>Annotation</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Annotation</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Annotation</em>' attribute.
   * @see #setAnnotation(String)
   * @see org.xtext.example.tfg.myDsl.MyDslPackage#getANNOTATED_ATTRIBUTE_Annotation()
   * @model
   * @generated
   */
  String getAnnotation();

  /**
   * Sets the value of the '{@link org.xtext.example.tfg.myDsl.ANNOTATED_ATTRIBUTE#getAnnotation <em>Annotation</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Annotation</em>' attribute.
   * @see #getAnnotation()
   * @generated
   */
  void setAnnotation(String value);

  /**
   * Returns the value of the '<em><b>Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Type</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' containment reference.
   * @see #setType(JvmTypeReference)
   * @see org.xtext.example.tfg.myDsl.MyDslPackage#getANNOTATED_ATTRIBUTE_Type()
   * @model containment="true"
   * @generated
   */
  JvmTypeReference getType();

  /**
   * Sets the value of the '{@link org.xtext.example.tfg.myDsl.ANNOTATED_ATTRIBUTE#getType <em>Type</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' containment reference.
   * @see #getType()
   * @generated
   */
  void setType(JvmTypeReference value);

} // ANNOTATED_ATTRIBUTE
