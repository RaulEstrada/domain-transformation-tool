/**
 * generated by Xtext 2.9.0
 */
package org.xtext.example.tfg.myDsl.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.xtext.example.tfg.myDsl.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.xtext.example.tfg.myDsl.MyDslPackage
 * @generated
 */
public class MyDslSwitch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static MyDslPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MyDslSwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = MyDslPackage.eINSTANCE;
    }
  }

  /**
   * Checks whether this is a switch for the given package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param ePackage the package in question.
   * @return whether this is a switch for the given package.
   * @generated
   */
  @Override
  protected boolean isSwitchFor(EPackage ePackage)
  {
    return ePackage == modelPackage;
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  @Override
  protected T doSwitch(int classifierID, EObject theEObject)
  {
    switch (classifierID)
    {
      case MyDslPackage.MODEL:
      {
        MODEL model = (MODEL)theEObject;
        T result = caseMODEL(model);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MyDslPackage.ELEMENT:
      {
        ELEMENT element = (ELEMENT)theEObject;
        T result = caseELEMENT(element);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MyDslPackage.ENTITY:
      {
        ENTITY entity = (ENTITY)theEObject;
        T result = caseENTITY(entity);
        if (result == null) result = caseELEMENT(entity);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MyDslPackage.ENTITYELEMENT:
      {
        ENTITYELEMENT entityelement = (ENTITYELEMENT)theEObject;
        T result = caseENTITYELEMENT(entityelement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MyDslPackage.VALUEOBJECT:
      {
        VALUEOBJECT valueobject = (VALUEOBJECT)theEObject;
        T result = caseVALUEOBJECT(valueobject);
        if (result == null) result = caseELEMENT(valueobject);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MyDslPackage.ENUMERATION:
      {
        ENUMERATION enumeration = (ENUMERATION)theEObject;
        T result = caseENUMERATION(enumeration);
        if (result == null) result = caseELEMENT(enumeration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MyDslPackage.LINK:
      {
        LINK link = (LINK)theEObject;
        T result = caseLINK(link);
        if (result == null) result = caseELEMENT(link);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MyDslPackage.ANNOTATED_ATTRIBUTE:
      {
        ANNOTATED_ATTRIBUTE annotateD_ATTRIBUTE = (ANNOTATED_ATTRIBUTE)theEObject;
        T result = caseANNOTATED_ATTRIBUTE(annotateD_ATTRIBUTE);
        if (result == null) result = caseENTITYELEMENT(annotateD_ATTRIBUTE);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MyDslPackage.ATTRIBUTE:
      {
        ATTRIBUTE attribute = (ATTRIBUTE)theEObject;
        T result = caseATTRIBUTE(attribute);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MyDslPackage.RELATIONSHIP:
      {
        RELATIONSHIP relationship = (RELATIONSHIP)theEObject;
        T result = caseRELATIONSHIP(relationship);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MyDslPackage.OPERATION:
      {
        OPERATION operation = (OPERATION)theEObject;
        T result = caseOPERATION(operation);
        if (result == null) result = caseENTITYELEMENT(operation);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      default: return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>MODEL</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>MODEL</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseMODEL(MODEL object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>ELEMENT</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>ELEMENT</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseELEMENT(ELEMENT object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>ENTITY</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>ENTITY</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseENTITY(ENTITY object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>ENTITYELEMENT</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>ENTITYELEMENT</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseENTITYELEMENT(ENTITYELEMENT object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>VALUEOBJECT</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>VALUEOBJECT</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseVALUEOBJECT(VALUEOBJECT object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>ENUMERATION</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>ENUMERATION</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseENUMERATION(ENUMERATION object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>LINK</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>LINK</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseLINK(LINK object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>ANNOTATED ATTRIBUTE</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>ANNOTATED ATTRIBUTE</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseANNOTATED_ATTRIBUTE(ANNOTATED_ATTRIBUTE object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>ATTRIBUTE</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>ATTRIBUTE</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseATTRIBUTE(ATTRIBUTE object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>RELATIONSHIP</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>RELATIONSHIP</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseRELATIONSHIP(RELATIONSHIP object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>OPERATION</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>OPERATION</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseOPERATION(OPERATION object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch, but this is the last case anyway.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject)
   * @generated
   */
  @Override
  public T defaultCase(EObject object)
  {
    return null;
  }

} //MyDslSwitch
