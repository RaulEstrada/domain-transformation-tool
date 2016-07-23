/**
 * generated by Xtext 2.9.0
 */
package org.xtext.example.tfg.myDsl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.xtext.example.tfg.myDsl.MyDslFactory
 * @model kind="package"
 * @generated
 */
public interface MyDslPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "myDsl";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.xtext.org/example/tfg/MyDsl";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "myDsl";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  MyDslPackage eINSTANCE = org.xtext.example.tfg.myDsl.impl.MyDslPackageImpl.init();

  /**
   * The meta object id for the '{@link org.xtext.example.tfg.myDsl.impl.MODELImpl <em>MODEL</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.xtext.example.tfg.myDsl.impl.MODELImpl
   * @see org.xtext.example.tfg.myDsl.impl.MyDslPackageImpl#getMODEL()
   * @generated
   */
  int MODEL = 0;

  /**
   * The feature id for the '<em><b>Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL__ELEMENTS = 0;

  /**
   * The number of structural features of the '<em>MODEL</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link org.xtext.example.tfg.myDsl.impl.ELEMENTImpl <em>ELEMENT</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.xtext.example.tfg.myDsl.impl.ELEMENTImpl
   * @see org.xtext.example.tfg.myDsl.impl.MyDslPackageImpl#getELEMENT()
   * @generated
   */
  int ELEMENT = 1;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ELEMENT__NAME = 0;

  /**
   * The number of structural features of the '<em>ELEMENT</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ELEMENT_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link org.xtext.example.tfg.myDsl.impl.ENTITYImpl <em>ENTITY</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.xtext.example.tfg.myDsl.impl.ENTITYImpl
   * @see org.xtext.example.tfg.myDsl.impl.MyDslPackageImpl#getENTITY()
   * @generated
   */
  int ENTITY = 2;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENTITY__NAME = ELEMENT__NAME;

  /**
   * The feature id for the '<em><b>Abstract Entity</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENTITY__ABSTRACT_ENTITY = ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Parent</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENTITY__PARENT = ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Entity Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENTITY__ENTITY_ELEMENTS = ELEMENT_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>ENTITY</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENTITY_FEATURE_COUNT = ELEMENT_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link org.xtext.example.tfg.myDsl.impl.ENTITYELEMENTImpl <em>ENTITYELEMENT</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.xtext.example.tfg.myDsl.impl.ENTITYELEMENTImpl
   * @see org.xtext.example.tfg.myDsl.impl.MyDslPackageImpl#getENTITYELEMENT()
   * @generated
   */
  int ENTITYELEMENT = 3;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENTITYELEMENT__NAME = 0;

  /**
   * The number of structural features of the '<em>ENTITYELEMENT</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENTITYELEMENT_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link org.xtext.example.tfg.myDsl.impl.VALUEOBJECTImpl <em>VALUEOBJECT</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.xtext.example.tfg.myDsl.impl.VALUEOBJECTImpl
   * @see org.xtext.example.tfg.myDsl.impl.MyDslPackageImpl#getVALUEOBJECT()
   * @generated
   */
  int VALUEOBJECT = 4;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VALUEOBJECT__NAME = ELEMENT__NAME;

  /**
   * The feature id for the '<em><b>Valueattributes</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VALUEOBJECT__VALUEATTRIBUTES = ELEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>VALUEOBJECT</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VALUEOBJECT_FEATURE_COUNT = ELEMENT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.xtext.example.tfg.myDsl.impl.ENUMERATIONImpl <em>ENUMERATION</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.xtext.example.tfg.myDsl.impl.ENUMERATIONImpl
   * @see org.xtext.example.tfg.myDsl.impl.MyDslPackageImpl#getENUMERATION()
   * @generated
   */
  int ENUMERATION = 5;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENUMERATION__NAME = ELEMENT__NAME;

  /**
   * The feature id for the '<em><b>Enumelements</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENUMERATION__ENUMELEMENTS = ELEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>ENUMERATION</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENUMERATION_FEATURE_COUNT = ELEMENT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.xtext.example.tfg.myDsl.impl.LINKImpl <em>LINK</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.xtext.example.tfg.myDsl.impl.LINKImpl
   * @see org.xtext.example.tfg.myDsl.impl.MyDslPackageImpl#getLINK()
   * @generated
   */
  int LINK = 6;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LINK__NAME = ELEMENT__NAME;

  /**
   * The feature id for the '<em><b>Relations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LINK__RELATIONS = ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Attributes</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LINK__ATTRIBUTES = ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>LINK</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LINK_FEATURE_COUNT = ELEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.xtext.example.tfg.myDsl.impl.ANNOTATED_ATTRIBUTEImpl <em>ANNOTATED ATTRIBUTE</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.xtext.example.tfg.myDsl.impl.ANNOTATED_ATTRIBUTEImpl
   * @see org.xtext.example.tfg.myDsl.impl.MyDslPackageImpl#getANNOTATED_ATTRIBUTE()
   * @generated
   */
  int ANNOTATED_ATTRIBUTE = 7;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ANNOTATED_ATTRIBUTE__NAME = ENTITYELEMENT__NAME;

  /**
   * The feature id for the '<em><b>Annotation</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ANNOTATED_ATTRIBUTE__ANNOTATION = ENTITYELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ANNOTATED_ATTRIBUTE__TYPE = ENTITYELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>ANNOTATED ATTRIBUTE</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ANNOTATED_ATTRIBUTE_FEATURE_COUNT = ENTITYELEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.xtext.example.tfg.myDsl.impl.ATTRIBUTEImpl <em>ATTRIBUTE</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.xtext.example.tfg.myDsl.impl.ATTRIBUTEImpl
   * @see org.xtext.example.tfg.myDsl.impl.MyDslPackageImpl#getATTRIBUTE()
   * @generated
   */
  int ATTRIBUTE = 8;

  /**
   * The feature id for the '<em><b>Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTRIBUTE__TYPE = 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTRIBUTE__NAME = 1;

  /**
   * The number of structural features of the '<em>ATTRIBUTE</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTRIBUTE_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link org.xtext.example.tfg.myDsl.impl.RELATIONSHIPImpl <em>RELATIONSHIP</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.xtext.example.tfg.myDsl.impl.RELATIONSHIPImpl
   * @see org.xtext.example.tfg.myDsl.impl.MyDslPackageImpl#getRELATIONSHIP()
   * @generated
   */
  int RELATIONSHIP = 9;

  /**
   * The feature id for the '<em><b>Cardinal</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RELATIONSHIP__CARDINAL = 0;

  /**
   * The feature id for the '<em><b>Type</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RELATIONSHIP__TYPE = 1;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RELATIONSHIP__NAME = 2;

  /**
   * The number of structural features of the '<em>RELATIONSHIP</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RELATIONSHIP_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link org.xtext.example.tfg.myDsl.impl.OPERATIONImpl <em>OPERATION</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.xtext.example.tfg.myDsl.impl.OPERATIONImpl
   * @see org.xtext.example.tfg.myDsl.impl.MyDslPackageImpl#getOPERATION()
   * @generated
   */
  int OPERATION = 10;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION__NAME = ENTITYELEMENT__NAME;

  /**
   * The feature id for the '<em><b>Return</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION__RETURN = ENTITYELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Params</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION__PARAMS = ENTITYELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>OPERATION</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION_FEATURE_COUNT = ENTITYELEMENT_FEATURE_COUNT + 2;


  /**
   * Returns the meta object for class '{@link org.xtext.example.tfg.myDsl.MODEL <em>MODEL</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>MODEL</em>'.
   * @see org.xtext.example.tfg.myDsl.MODEL
   * @generated
   */
  EClass getMODEL();

  /**
   * Returns the meta object for the containment reference list '{@link org.xtext.example.tfg.myDsl.MODEL#getElements <em>Elements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Elements</em>'.
   * @see org.xtext.example.tfg.myDsl.MODEL#getElements()
   * @see #getMODEL()
   * @generated
   */
  EReference getMODEL_Elements();

  /**
   * Returns the meta object for class '{@link org.xtext.example.tfg.myDsl.ELEMENT <em>ELEMENT</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>ELEMENT</em>'.
   * @see org.xtext.example.tfg.myDsl.ELEMENT
   * @generated
   */
  EClass getELEMENT();

  /**
   * Returns the meta object for the attribute '{@link org.xtext.example.tfg.myDsl.ELEMENT#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.xtext.example.tfg.myDsl.ELEMENT#getName()
   * @see #getELEMENT()
   * @generated
   */
  EAttribute getELEMENT_Name();

  /**
   * Returns the meta object for class '{@link org.xtext.example.tfg.myDsl.ENTITY <em>ENTITY</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>ENTITY</em>'.
   * @see org.xtext.example.tfg.myDsl.ENTITY
   * @generated
   */
  EClass getENTITY();

  /**
   * Returns the meta object for the attribute '{@link org.xtext.example.tfg.myDsl.ENTITY#isAbstractEntity <em>Abstract Entity</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Abstract Entity</em>'.
   * @see org.xtext.example.tfg.myDsl.ENTITY#isAbstractEntity()
   * @see #getENTITY()
   * @generated
   */
  EAttribute getENTITY_AbstractEntity();

  /**
   * Returns the meta object for the reference '{@link org.xtext.example.tfg.myDsl.ENTITY#getParent <em>Parent</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Parent</em>'.
   * @see org.xtext.example.tfg.myDsl.ENTITY#getParent()
   * @see #getENTITY()
   * @generated
   */
  EReference getENTITY_Parent();

  /**
   * Returns the meta object for the containment reference list '{@link org.xtext.example.tfg.myDsl.ENTITY#getEntityElements <em>Entity Elements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Entity Elements</em>'.
   * @see org.xtext.example.tfg.myDsl.ENTITY#getEntityElements()
   * @see #getENTITY()
   * @generated
   */
  EReference getENTITY_EntityElements();

  /**
   * Returns the meta object for class '{@link org.xtext.example.tfg.myDsl.ENTITYELEMENT <em>ENTITYELEMENT</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>ENTITYELEMENT</em>'.
   * @see org.xtext.example.tfg.myDsl.ENTITYELEMENT
   * @generated
   */
  EClass getENTITYELEMENT();

  /**
   * Returns the meta object for the attribute '{@link org.xtext.example.tfg.myDsl.ENTITYELEMENT#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.xtext.example.tfg.myDsl.ENTITYELEMENT#getName()
   * @see #getENTITYELEMENT()
   * @generated
   */
  EAttribute getENTITYELEMENT_Name();

  /**
   * Returns the meta object for class '{@link org.xtext.example.tfg.myDsl.VALUEOBJECT <em>VALUEOBJECT</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>VALUEOBJECT</em>'.
   * @see org.xtext.example.tfg.myDsl.VALUEOBJECT
   * @generated
   */
  EClass getVALUEOBJECT();

  /**
   * Returns the meta object for the containment reference list '{@link org.xtext.example.tfg.myDsl.VALUEOBJECT#getValueattributes <em>Valueattributes</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Valueattributes</em>'.
   * @see org.xtext.example.tfg.myDsl.VALUEOBJECT#getValueattributes()
   * @see #getVALUEOBJECT()
   * @generated
   */
  EReference getVALUEOBJECT_Valueattributes();

  /**
   * Returns the meta object for class '{@link org.xtext.example.tfg.myDsl.ENUMERATION <em>ENUMERATION</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>ENUMERATION</em>'.
   * @see org.xtext.example.tfg.myDsl.ENUMERATION
   * @generated
   */
  EClass getENUMERATION();

  /**
   * Returns the meta object for the attribute list '{@link org.xtext.example.tfg.myDsl.ENUMERATION#getEnumelements <em>Enumelements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Enumelements</em>'.
   * @see org.xtext.example.tfg.myDsl.ENUMERATION#getEnumelements()
   * @see #getENUMERATION()
   * @generated
   */
  EAttribute getENUMERATION_Enumelements();

  /**
   * Returns the meta object for class '{@link org.xtext.example.tfg.myDsl.LINK <em>LINK</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>LINK</em>'.
   * @see org.xtext.example.tfg.myDsl.LINK
   * @generated
   */
  EClass getLINK();

  /**
   * Returns the meta object for the containment reference list '{@link org.xtext.example.tfg.myDsl.LINK#getRelations <em>Relations</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Relations</em>'.
   * @see org.xtext.example.tfg.myDsl.LINK#getRelations()
   * @see #getLINK()
   * @generated
   */
  EReference getLINK_Relations();

  /**
   * Returns the meta object for the containment reference list '{@link org.xtext.example.tfg.myDsl.LINK#getAttributes <em>Attributes</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Attributes</em>'.
   * @see org.xtext.example.tfg.myDsl.LINK#getAttributes()
   * @see #getLINK()
   * @generated
   */
  EReference getLINK_Attributes();

  /**
   * Returns the meta object for class '{@link org.xtext.example.tfg.myDsl.ANNOTATED_ATTRIBUTE <em>ANNOTATED ATTRIBUTE</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>ANNOTATED ATTRIBUTE</em>'.
   * @see org.xtext.example.tfg.myDsl.ANNOTATED_ATTRIBUTE
   * @generated
   */
  EClass getANNOTATED_ATTRIBUTE();

  /**
   * Returns the meta object for the attribute '{@link org.xtext.example.tfg.myDsl.ANNOTATED_ATTRIBUTE#getAnnotation <em>Annotation</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Annotation</em>'.
   * @see org.xtext.example.tfg.myDsl.ANNOTATED_ATTRIBUTE#getAnnotation()
   * @see #getANNOTATED_ATTRIBUTE()
   * @generated
   */
  EAttribute getANNOTATED_ATTRIBUTE_Annotation();

  /**
   * Returns the meta object for the containment reference '{@link org.xtext.example.tfg.myDsl.ANNOTATED_ATTRIBUTE#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Type</em>'.
   * @see org.xtext.example.tfg.myDsl.ANNOTATED_ATTRIBUTE#getType()
   * @see #getANNOTATED_ATTRIBUTE()
   * @generated
   */
  EReference getANNOTATED_ATTRIBUTE_Type();

  /**
   * Returns the meta object for class '{@link org.xtext.example.tfg.myDsl.ATTRIBUTE <em>ATTRIBUTE</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>ATTRIBUTE</em>'.
   * @see org.xtext.example.tfg.myDsl.ATTRIBUTE
   * @generated
   */
  EClass getATTRIBUTE();

  /**
   * Returns the meta object for the containment reference '{@link org.xtext.example.tfg.myDsl.ATTRIBUTE#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Type</em>'.
   * @see org.xtext.example.tfg.myDsl.ATTRIBUTE#getType()
   * @see #getATTRIBUTE()
   * @generated
   */
  EReference getATTRIBUTE_Type();

  /**
   * Returns the meta object for the attribute '{@link org.xtext.example.tfg.myDsl.ATTRIBUTE#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.xtext.example.tfg.myDsl.ATTRIBUTE#getName()
   * @see #getATTRIBUTE()
   * @generated
   */
  EAttribute getATTRIBUTE_Name();

  /**
   * Returns the meta object for class '{@link org.xtext.example.tfg.myDsl.RELATIONSHIP <em>RELATIONSHIP</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>RELATIONSHIP</em>'.
   * @see org.xtext.example.tfg.myDsl.RELATIONSHIP
   * @generated
   */
  EClass getRELATIONSHIP();

  /**
   * Returns the meta object for the attribute '{@link org.xtext.example.tfg.myDsl.RELATIONSHIP#getCardinal <em>Cardinal</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Cardinal</em>'.
   * @see org.xtext.example.tfg.myDsl.RELATIONSHIP#getCardinal()
   * @see #getRELATIONSHIP()
   * @generated
   */
  EAttribute getRELATIONSHIP_Cardinal();

  /**
   * Returns the meta object for the reference '{@link org.xtext.example.tfg.myDsl.RELATIONSHIP#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Type</em>'.
   * @see org.xtext.example.tfg.myDsl.RELATIONSHIP#getType()
   * @see #getRELATIONSHIP()
   * @generated
   */
  EReference getRELATIONSHIP_Type();

  /**
   * Returns the meta object for the attribute '{@link org.xtext.example.tfg.myDsl.RELATIONSHIP#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.xtext.example.tfg.myDsl.RELATIONSHIP#getName()
   * @see #getRELATIONSHIP()
   * @generated
   */
  EAttribute getRELATIONSHIP_Name();

  /**
   * Returns the meta object for class '{@link org.xtext.example.tfg.myDsl.OPERATION <em>OPERATION</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>OPERATION</em>'.
   * @see org.xtext.example.tfg.myDsl.OPERATION
   * @generated
   */
  EClass getOPERATION();

  /**
   * Returns the meta object for the containment reference '{@link org.xtext.example.tfg.myDsl.OPERATION#getReturn <em>Return</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Return</em>'.
   * @see org.xtext.example.tfg.myDsl.OPERATION#getReturn()
   * @see #getOPERATION()
   * @generated
   */
  EReference getOPERATION_Return();

  /**
   * Returns the meta object for the containment reference list '{@link org.xtext.example.tfg.myDsl.OPERATION#getParams <em>Params</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Params</em>'.
   * @see org.xtext.example.tfg.myDsl.OPERATION#getParams()
   * @see #getOPERATION()
   * @generated
   */
  EReference getOPERATION_Params();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  MyDslFactory getMyDslFactory();

  /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  interface Literals
  {
    /**
     * The meta object literal for the '{@link org.xtext.example.tfg.myDsl.impl.MODELImpl <em>MODEL</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.xtext.example.tfg.myDsl.impl.MODELImpl
     * @see org.xtext.example.tfg.myDsl.impl.MyDslPackageImpl#getMODEL()
     * @generated
     */
    EClass MODEL = eINSTANCE.getMODEL();

    /**
     * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MODEL__ELEMENTS = eINSTANCE.getMODEL_Elements();

    /**
     * The meta object literal for the '{@link org.xtext.example.tfg.myDsl.impl.ELEMENTImpl <em>ELEMENT</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.xtext.example.tfg.myDsl.impl.ELEMENTImpl
     * @see org.xtext.example.tfg.myDsl.impl.MyDslPackageImpl#getELEMENT()
     * @generated
     */
    EClass ELEMENT = eINSTANCE.getELEMENT();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ELEMENT__NAME = eINSTANCE.getELEMENT_Name();

    /**
     * The meta object literal for the '{@link org.xtext.example.tfg.myDsl.impl.ENTITYImpl <em>ENTITY</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.xtext.example.tfg.myDsl.impl.ENTITYImpl
     * @see org.xtext.example.tfg.myDsl.impl.MyDslPackageImpl#getENTITY()
     * @generated
     */
    EClass ENTITY = eINSTANCE.getENTITY();

    /**
     * The meta object literal for the '<em><b>Abstract Entity</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ENTITY__ABSTRACT_ENTITY = eINSTANCE.getENTITY_AbstractEntity();

    /**
     * The meta object literal for the '<em><b>Parent</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ENTITY__PARENT = eINSTANCE.getENTITY_Parent();

    /**
     * The meta object literal for the '<em><b>Entity Elements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ENTITY__ENTITY_ELEMENTS = eINSTANCE.getENTITY_EntityElements();

    /**
     * The meta object literal for the '{@link org.xtext.example.tfg.myDsl.impl.ENTITYELEMENTImpl <em>ENTITYELEMENT</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.xtext.example.tfg.myDsl.impl.ENTITYELEMENTImpl
     * @see org.xtext.example.tfg.myDsl.impl.MyDslPackageImpl#getENTITYELEMENT()
     * @generated
     */
    EClass ENTITYELEMENT = eINSTANCE.getENTITYELEMENT();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ENTITYELEMENT__NAME = eINSTANCE.getENTITYELEMENT_Name();

    /**
     * The meta object literal for the '{@link org.xtext.example.tfg.myDsl.impl.VALUEOBJECTImpl <em>VALUEOBJECT</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.xtext.example.tfg.myDsl.impl.VALUEOBJECTImpl
     * @see org.xtext.example.tfg.myDsl.impl.MyDslPackageImpl#getVALUEOBJECT()
     * @generated
     */
    EClass VALUEOBJECT = eINSTANCE.getVALUEOBJECT();

    /**
     * The meta object literal for the '<em><b>Valueattributes</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference VALUEOBJECT__VALUEATTRIBUTES = eINSTANCE.getVALUEOBJECT_Valueattributes();

    /**
     * The meta object literal for the '{@link org.xtext.example.tfg.myDsl.impl.ENUMERATIONImpl <em>ENUMERATION</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.xtext.example.tfg.myDsl.impl.ENUMERATIONImpl
     * @see org.xtext.example.tfg.myDsl.impl.MyDslPackageImpl#getENUMERATION()
     * @generated
     */
    EClass ENUMERATION = eINSTANCE.getENUMERATION();

    /**
     * The meta object literal for the '<em><b>Enumelements</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ENUMERATION__ENUMELEMENTS = eINSTANCE.getENUMERATION_Enumelements();

    /**
     * The meta object literal for the '{@link org.xtext.example.tfg.myDsl.impl.LINKImpl <em>LINK</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.xtext.example.tfg.myDsl.impl.LINKImpl
     * @see org.xtext.example.tfg.myDsl.impl.MyDslPackageImpl#getLINK()
     * @generated
     */
    EClass LINK = eINSTANCE.getLINK();

    /**
     * The meta object literal for the '<em><b>Relations</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference LINK__RELATIONS = eINSTANCE.getLINK_Relations();

    /**
     * The meta object literal for the '<em><b>Attributes</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference LINK__ATTRIBUTES = eINSTANCE.getLINK_Attributes();

    /**
     * The meta object literal for the '{@link org.xtext.example.tfg.myDsl.impl.ANNOTATED_ATTRIBUTEImpl <em>ANNOTATED ATTRIBUTE</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.xtext.example.tfg.myDsl.impl.ANNOTATED_ATTRIBUTEImpl
     * @see org.xtext.example.tfg.myDsl.impl.MyDslPackageImpl#getANNOTATED_ATTRIBUTE()
     * @generated
     */
    EClass ANNOTATED_ATTRIBUTE = eINSTANCE.getANNOTATED_ATTRIBUTE();

    /**
     * The meta object literal for the '<em><b>Annotation</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ANNOTATED_ATTRIBUTE__ANNOTATION = eINSTANCE.getANNOTATED_ATTRIBUTE_Annotation();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ANNOTATED_ATTRIBUTE__TYPE = eINSTANCE.getANNOTATED_ATTRIBUTE_Type();

    /**
     * The meta object literal for the '{@link org.xtext.example.tfg.myDsl.impl.ATTRIBUTEImpl <em>ATTRIBUTE</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.xtext.example.tfg.myDsl.impl.ATTRIBUTEImpl
     * @see org.xtext.example.tfg.myDsl.impl.MyDslPackageImpl#getATTRIBUTE()
     * @generated
     */
    EClass ATTRIBUTE = eINSTANCE.getATTRIBUTE();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ATTRIBUTE__TYPE = eINSTANCE.getATTRIBUTE_Type();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ATTRIBUTE__NAME = eINSTANCE.getATTRIBUTE_Name();

    /**
     * The meta object literal for the '{@link org.xtext.example.tfg.myDsl.impl.RELATIONSHIPImpl <em>RELATIONSHIP</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.xtext.example.tfg.myDsl.impl.RELATIONSHIPImpl
     * @see org.xtext.example.tfg.myDsl.impl.MyDslPackageImpl#getRELATIONSHIP()
     * @generated
     */
    EClass RELATIONSHIP = eINSTANCE.getRELATIONSHIP();

    /**
     * The meta object literal for the '<em><b>Cardinal</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute RELATIONSHIP__CARDINAL = eINSTANCE.getRELATIONSHIP_Cardinal();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference RELATIONSHIP__TYPE = eINSTANCE.getRELATIONSHIP_Type();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute RELATIONSHIP__NAME = eINSTANCE.getRELATIONSHIP_Name();

    /**
     * The meta object literal for the '{@link org.xtext.example.tfg.myDsl.impl.OPERATIONImpl <em>OPERATION</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.xtext.example.tfg.myDsl.impl.OPERATIONImpl
     * @see org.xtext.example.tfg.myDsl.impl.MyDslPackageImpl#getOPERATION()
     * @generated
     */
    EClass OPERATION = eINSTANCE.getOPERATION();

    /**
     * The meta object literal for the '<em><b>Return</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OPERATION__RETURN = eINSTANCE.getOPERATION_Return();

    /**
     * The meta object literal for the '<em><b>Params</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference OPERATION__PARAMS = eINSTANCE.getOPERATION_Params();

  }

} //MyDslPackage
