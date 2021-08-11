import "react-credit-cards/es/styles-compiled.css";
import styled from 'styled-components';
import GooglePayButton from '@google-pay/button-react';

const Row = styled.div`
  width: 100%;
  display: flex;
  flex-direction: row;

  .btn {
    margin: 0px 5px;
  }

  .google-pay-button-container {
    div {
      button {
        width: 100%;
      }
    }
    width: 100%;
  }
`;

const PaymentComponent = (props) => {
  return (
    <Row>
			<GooglePayButton
        environment="TEST"
        paymentRequest={{
          apiVersion: 2,
          apiVersionMinor: 0,
          allowedPaymentMethods: [
            {
              type: 'CARD',
              parameters: {
                allowedAuthMethods: ['PAN_ONLY', 'CRYPTOGRAM_3DS'],
                allowedCardNetworks: ['MASTERCARD', 'MAESTRO', 'VISA'],
              },
              tokenizationSpecification: {
                type: 'PAYMENT_GATEWAY',
                parameters: {
                  gateway: 'example',
                  gatewayMerchantId: 'exampleGatewayMerchantId',
                },
              },
            },
          ],
          merchantInfo: {
            merchantId: '12345678901234567890',
            merchantName: 'Car rental',
          },
          transactionInfo: {
            totalPriceStatus: 'FINAL',
            totalPriceLabel: 'Total',
            totalPrice: `${props.data.totalCost}`,
            currencyCode: 'BYN',
            countryCode: 'BR',
          },
          callbackIntents: ['PAYMENT_AUTHORIZATION']
        }}
        onPaymentAuthorized={paymentData => {
          props.payBill(props.data.id);
          props.handlePayBillCancel();
          return {transactionState: 'SUCCESS'}
        }}
        existingPaymentMethodRequired= 'false'
        buttonColor = 'black'
        buttonType = 'pay'
      />
    </Row>  
  )
}

export default PaymentComponent
