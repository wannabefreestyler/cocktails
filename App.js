
import React, { useState } from 'react';
import axios from 'axios';
import styled from 'styled-components';

const Container = styled.div`
    background-image: url('/back.jpg');
    background-size: cover;
    background-position: center;
    height: 100vh;
    width: 100vw;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    text-align: center;
    overflow: hidden;
    margin: 0;
    padding: 0;
    box-sizing: border-box;
`;

const HeaderContainer = styled.div`
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    background-color: transparent;
    padding: 10px;
    box-sizing: border-box;
    z-index: 1;
`;

const ContentContainer = styled.div`
  margin-top: 60px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  border-radius: 10px;
  background-color: #9a3131;
  padding: 20px;
  width: 50%;
  border: 2px solid black;
  box-sizing: border-box;
`;

const Title = styled.h1`
  background-image: linear-gradient(to right, #ff0000 0%, #000000 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  font-size: 4rem;
  margin-bottom: 20px;
`;

const ButtonContainer = styled.div`
    display: flex;
    justify-content: center;
    margin-top: 50px;
`;

const Input = styled.input`
    padding: 15px 30px;
    margin-right: 10px;
    font-size: 24px;
    border: 2px solid #000;
    border-radius: 5px;
`;

const Button = styled.button`
  padding: 15px 30px;
  margin: 10px;
  background-color: #000000;
  color: #ff0000;
  border: 2px solid #ffffff;
  border-radius: 5px;
  cursor: pointer;
  font-size: 24px;
  text-transform: uppercase;
  text-shadow: 1px 1px 1px #000;
`;

const CocktailInfo = styled.div`
  text-align: center;
  color: #000000;
  background-color: #9a3131;
  border-radius: 10px;
  padding: 20px;
  font-size: 36px;
  text-shadow: 1px 1px 1px #ffffff;
`;

const IngredientInfo = styled.div`
  text-align: center;
  color: #000000;
  background-color: #9a3131;
  border-radius: 10px;
  padding: 20px;
  font-size: 36px;
  text-shadow: 1px 1px 1px #ffffff;
`;

const SuccessMessage = styled.div`
  text-align: center;
  color: #000000;
  background-color: #9a3131;
  border-radius: 10px;
  padding: 20px;
  font-size: 36px;
  text-shadow: 1px 1px 1px #ffffff;
`;

const ErrorMessage = styled.div`
  color: #9a3131;
  margin-top: 10px;
`;

const BackButton = styled(Button)`
  position: fixed;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  background-color: #572b2b;
`;

function App() {
  const [step, setStep] = useState('select');
  const [name, setName] = useState('');
  const [category, setCategory] = useState('');
  const [instruction, setInstruction] = useState('');
  const [id, setId] = useState('');
  const [newName, setNewName] = useState('');
  const [newCategory, setNewCategory] = useState('');
  const [newInstruction, setNewInstruction] = useState('');
  const [cocktail, setCocktail] = useState(null);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState(null);
  const [ingredientId, setIngredientId] = useState('');
  const [cocktails, setCocktails] = useState([]);
  const [ingredient, setIngredient] = useState(null);

  const resetState = () => {
    setStep('select');
    setName('');
    setCategory('');
    setInstruction('');
    setId('');
    setNewName('');
    setNewCategory('');
    setNewInstruction('');
    setCocktail(null);
    setSuccess(false);
    setError(null);
    setIngredientId('');
    setCocktails([]);
    setIngredient(null);
  };

  const handleAddCocktail = async () => {
    try {
      const response = await axios.post('http://localhost:8080/cocktails', { name, category, instruction});
      console.log(response.data);
      setStep('result');
      setSuccess(true);
      setError(null);
    } catch (error) {
      setError(error.response?.data || 'An error occurred');
    }
  };


  const handleGetCocktail = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/cocktails?name=${name}`);
      console.log(response.data);
      setCocktail(response.data);
      setStep('result');
      //setCocktail(response.data);
      setError(null);
    } catch (error) {
      setError(error.response?.data || 'An error occurred');
    }
  };

  const handleUpdateCocktail = async () => {
    try {
      const response = await axios.put(`http://localhost:8080/cocktails?name=${name}`, { name: newName, category: newCategory, instruction: newInstruction});
      console.log(response.data);
      setStep('result');
      setSuccess(true);
      setError(null);
    } catch (error) {
      setError(error.response?.data || 'An error occurred');
    }
  };

  const handleDeleteCocktail = async () => {
    try {
      const response = await axios.delete(`http://localhost:8080/cocktails`, { params: { name } });
      console.log(response.data);
      setCocktail(null);
      setSuccess(true);
      setError(null);
      setStep('result');
    } catch (error) {
      setError(error.response?.data || 'An error occurred');
    }
  };

  const handleGetIngredient = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/ingredients?id=${ingredientId}`);
      console.log(response.data);
      setIngredient(response.data);
      setStep('result');
      setError(null);
    } catch (error) {
      setError(error.response?.data || 'An error occurred');
    }
  };

  const getCocktailsWithIngredient = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/cocktails/with-ingredient?ingredientId=${ingredientId}`);
      console.log(response.data);
      setCocktails(response.data);
      setStep('result');
      setError(null);
    } catch (error) {
      setError(error.response?.data || 'An error occurred');
    }
  };


  return (
      <Container>
        <HeaderContainer>
          <Title>Cocktail Application</Title>
        </HeaderContainer>
        {step === 'select' && (
            <>
              <ButtonContainer>
                <Button onClick={() => setStep('add')}>Add Cocktail</Button>
                <Button onClick={() => setStep('get')}>Get Cocktail</Button>
                <Button onClick={() => setStep('update')}>Update Cocktail</Button>
                <Button onClick={() => setStep('delete')}>Delete Cocktail</Button>
                <Button onClick={() => setStep('getIngredient')}>Get Ingredient</Button>
                <Button onClick={() => setStep('getCocktailsWithIngredient')}>Get Cocktails With Ingredient</Button>
              </ButtonContainer>
            </>
        )}
        {step === 'add' && (
            <>
              <Input type="text" value={name} onChange={(e) => setName(e.target.value)} placeholder="Enter cocktail name" />
              <Input type="text" value={category} onChange={(e) => setCategory(e.target.value)} placeholder="Enter cocktail category" />
              <Input type="text" value={instruction} onChange={(e) => setInstruction(e.target.value)} placeholder="Enter cocktail instruction" />
              <Button onClick={handleAddCocktail}>Add Cocktail</Button>
            </>
        )}
        {step === 'get' && (
            <>
              <Input type="text" value={name} onChange={(e) => setName(e.target.value)} placeholder="Enter cocktail name" />
              <Button onClick={handleGetCocktail}>Get Cocktail</Button>
            </>
        )}
        {step === 'update' && (
            <>
              <Input type="text" value={name} onChange={(e) => setName(e.target.value)} placeholder="Enter cocktail name" />
              <Input type="text" value={newName} onChange={(e) => setNewName(e.target.value)} placeholder="Enter new cocktail name" />
              <Input type="text" value={newCategory} onChange={(e) => setNewCategory(e.target.value)} placeholder="Enter new cocktail category" />
              <Input type="text" value={newInstruction} onChange={(e) => setNewInstruction(e.target.value)} placeholder="Enter new cocktail instruction" />
              <Button onClick={handleUpdateCocktail}>Update Cocktail</Button>
            </>
        )}
        {step === 'delete' && (
            <>
              <Input type="text" value={name} onChange={(e) => setName(e.target.value)} placeholder="Enter cocktail name" />
              <Button onClick={handleDeleteCocktail}>Delete Cocktail</Button>
            </>
        )}
        {step === 'getIngredient' && (
            <>
              <Input type="text" value={ingredientId} onChange={(e) => setIngredientId(e.target.value)} placeholder="Enter ingredient ID" />
              <Button onClick={handleGetIngredient}>Get Ingredient</Button>
            </>
        )}
        {step === 'getCocktailsWithIngredient' && (
            <>
              <Input type="text" value={ingredientId} onChange={(e) => setIngredientId(e.target.value)} placeholder="Enter ingredient ID" />
              <Button onClick={getCocktailsWithIngredient}>Get Cocktails With Ingredient</Button>
            </>
        )}
        {step === 'result' && (
            <ContentContainer>
              {success && <SuccessMessage>Cocktail was successfully processed!</SuccessMessage>}
              {cocktail && (
                  <CocktailInfo>
                    <div>Cocktail: {cocktail.name}</div>
                    <div>Category: {cocktail.category}</div>
                    <div>Instruction: {cocktail.instruction}</div>
                  </CocktailInfo>
              )}
              {cocktails && cocktails.map((cocktail, index) => (
                  <CocktailInfo key={index}>
                    <div>Cocktail: {cocktail.name}</div>
                    <div>Category: {cocktail.category}</div>
                    <div>Instruction: {cocktail.instruction}</div>
                  </CocktailInfo>
              ))}
              {ingredient && (
                  <IngredientInfo>
                    <div>Ingredient: {ingredient.name}</div>
                  </IngredientInfo>

              )}
              {error && <ErrorMessage>Error: {error}</ErrorMessage>}
            </ContentContainer>
        )}
        <BackButton onClick={resetState}> Back </BackButton>
      </Container>
  );
}

export default App;
